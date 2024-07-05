/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import group.devtool.access.engine.Expression.ReferExpression;
import group.devtool.access.engine.Expression.LogicExpression;

/**
 * {@link ExpressionResolver} 默认实现类
 */
public class ExpressionResolverImpl implements ExpressionResolver {

  private int tokenIndex = 0;

  private int charIndex = 0;

  public ExpressionResolverImpl() {

  }

  @Override
  public Expression resolve(String expression) throws ExpressionException {
    List<Token> tokens = processToken(expression);
    if (tokens.isEmpty()) {
      return null;
    }
    Expression expr = syntaxParse(tokens);
    return expr;
  }

  private List<Token> processToken(String expression) throws ExpressionException {
    List<Token> tokens = new ArrayList<>();
    if (null == expression || expression.length() < 1) {
      return tokens;
    }
    String finalExpression = addTermination(expression);
    while (charIndex < finalExpression.length()) {
      char character = finalExpression.charAt(charIndex);
      switch (character) {
        case ' ':
          charIndex += 1;
          break;
        case '(':
          tokens.add(readFixedSize(finalExpression, TokenKind.LPAREN));
          break;
        case ')':
          tokens.add(readFixedSize(finalExpression, TokenKind.RPAREN));
          break;
        // !, !=
        case '!':
          tokens.add(readLongestSize(finalExpression, TokenKind.NE, TokenKind.NOT));
          break;
        case '&':
          tokens.add(readFixedSize(finalExpression, TokenKind.AND));
          break;
        case '|':
          tokens.add(readFixedSize(finalExpression, TokenKind.OR));
          break;
        // > >=
        case '>':
          tokens.add(readLongestSize(finalExpression, TokenKind.GE, TokenKind.GT));
          break;
        // < <=
        case '<':
          tokens.add(readLongestSize(finalExpression, TokenKind.LE, TokenKind.LT));
          break;
        case '=':
          tokens.add(readFixedSize(finalExpression, TokenKind.EQ));
          break;
        case '^':
          tokens.add(readFixedSize(finalExpression, TokenKind.BEGIN));
          break;
        case '$':
          tokens.add(readFixedSize(finalExpression, TokenKind.END));
          break;
        case '~':
          tokens.add(readFixedSize(finalExpression, TokenKind.IN));
          break;
        case '"':
          tokens.add(readUntilEof(finalExpression, TokenKind.STRING, charIndex + 1));
          break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          tokens.add(readUntilEof(finalExpression, TokenKind.NUMBER, charIndex));
          break;
        default:
          throw new ExpressionResolveException("表达式存在违法字符，字符：" + character);
      }
    }
    return tokens;
  }

  private String addTermination(String expression) {
    return expression + " ";
  }

  private Token readLongestSize(String expression, TokenKind... kinds)
      throws ExpressionResolveException {
    for (TokenKind kind : kinds) {
      String value = expression.substring(charIndex, charIndex + kind.getFixedSize());
      if (kind.match(value)) {
        Token token = new Token(value, kind);
        charIndex += kind.getFixedSize();
        return token;
      }
    }
    throw new ExpressionResolveException("表达式包含非法字符");
  }

  private Token readUntilEof(String expression, TokenKind kind, Integer beginIndex)
      throws ExpressionException {
    Token token;
    int index = beginIndex;
    while (index < expression.length()) {
      char character = expression.charAt(index);
      if (kind.getEof().apply(character)) {
        if (kind.includeEof()) {
          index += 1;
        }
        token = new Token(expression.substring(charIndex, index), kind);
        charIndex = index;
        return token;
      } else {
        index += 1;
      }
    }
    throw new ExpressionResolveException("未找到合适的表达式终结符");
  }

  private Token readFixedSize(String expression, TokenKind kind) throws ExpressionException {
    String value = expression.substring(charIndex, charIndex + kind.getFixedSize());
    if (kind.match(value)) {
      Token token = new Token(value, kind);
      charIndex += kind.getFixedSize();
      return token;
    }
    throw new ExpressionResolveException("表达式包含非法字符");

  }

  private Expression syntaxParse(List<Token> tokens) throws ExpressionException {
    Expression expr = orParse(tokens);
    if (tokenIndex < tokens.size()) {
      throw new ExpressionResolveException("表达式不合法，位置：" + tokenIndex);
    }
    return expr;
  }

  private Expression orParse(List<Token> tokens)
      throws ExpressionException {
    Expression left = andParse(tokens);
    if (tokenIndex < tokens.size() && tokens.get(tokenIndex).getKind().equals(TokenKind.OR)) {
      Operation operate = OperationProvider
          .getOperation(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      Expression right = orParse(tokens);
      return new LogicExpression(left, right, operate);
    }
    return left;
  }

  private Expression andParse(List<Token> tokens) throws ExpressionException {
    Expression left = notParse(tokens);
    if (tokenIndex < tokens.size() && tokens.get(tokenIndex).getKind().equals(TokenKind.AND)) {
      Operation operate = OperationProvider
          .getOperation(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      Expression right = orParse(tokens);
      return new LogicExpression(left, right, operate);
    }
    return left;
  }

  private Expression notParse(List<Token> tokens) throws ExpressionException {
    if (tokens.get(tokenIndex).getKind().equals(TokenKind.NOT)) {
      Operation operate = OperationProvider
          .getOperation(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      Expression paren = parenParse(tokens);
      if (null != paren) {
        return new LogicExpression(paren, null, operate);
      }
      tokenIndex += 1;
      ReferExpression left = new ReferExpression(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      return new LogicExpression(left, null, operate);
    }
    return parenParse(tokens);
  }

  private Expression parenParse(List<Token> tokens) throws ExpressionException {
    if (tokens.get(tokenIndex).getKind().equals(TokenKind.LPAREN)) {
      tokenIndex += 1;
      Expression sub = orParse(tokens);
      if (tokens.get(tokenIndex).getKind().equals(TokenKind.RPAREN)) {
        tokenIndex += 1;
      } else {
        throw new ExpressionResolveException("表达式解析异常，缺少闭合括号");
      }
      return sub;
    }
    return booleanParse(tokens);
  }

  private Expression booleanParse(List<Token> tokens) throws ExpressionException {
    ReferExpression left = new ReferExpression(tokens.get(tokenIndex).getValue());
    if (tokenIndex + 1 < tokens.size() && notLogic(tokens.get(tokenIndex + 1))) {
      tokenIndex += 1;
      Operation operation = OperationProvider.getOperation(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      ReferExpression right = new ReferExpression(tokens.get(tokenIndex).getValue());
      tokenIndex += 1;
      return new LogicExpression(left, right, operation);
    }
    tokenIndex += 1;
    return left;
  }

  private boolean notLogic(Token token) {
    TokenKind kind = token.getKind();
    return kind.equals(TokenKind.EQ)
        || kind.equals(TokenKind.NE)
        || kind.equals(TokenKind.GT)
        || kind.equals(TokenKind.GE)
        || kind.equals(TokenKind.LT)
        || kind.equals(TokenKind.LE)
        || kind.equals(TokenKind.IN)
        || kind.equals(TokenKind.BEGIN)
        || kind.equals(TokenKind.END);
  }

  public static class Token {

    private String value;

    private TokenKind kind;

    public Token(String value, TokenKind kind) {
      this.value = value;
      this.kind = kind;
    }

    public TokenKind getKind() {
      return kind;
    }

    public String getValue() {
      return value;
    }

  }

  public enum TokenKind {

    LPAREN("(", 1),
    RPAREN(")", 1),

    NOT("!", 1),
    AND("&&", 2),
    OR("||", 2),

    LT("<", 1),
    LE("<=", 2),
    GT(">", 1),
    GE(">=", 2),
    EQ("==", 2),
    NE("!=", 2),

    IN("~", 1),
    BEGIN("^", 1),
    END("$", 1),

    STRING((c) -> {
      return c == '"';
    }),
    NUMBER((c) -> {
      return !"0123456789".contains(c.toString());
    }, false)

    ;

    private Integer size;

    private Function<Character, Boolean> eof;

    private boolean includeEof;

    private String value;

    private TokenKind(String value, int size) {
      this.size = size;
      this.value = value;
    }

    private TokenKind(Function<Character, Boolean> eof) {
      this(eof, true);
    }

    private TokenKind(Function<Character, Boolean> eof, boolean includeEof) {
      this.value = null;
      this.eof = eof;
      this.includeEof = includeEof;
    }

    public Integer getFixedSize() {
      return size;
    }

    public Function<Character, Boolean> getEof() {
      return eof;
    }

    public boolean includeEof() {
      return includeEof;
    }

    public boolean match(String value) {
      return this.value.equals(value);
    }

  }
}
