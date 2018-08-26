package slim.other;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.udojava.evalex.Expression;

public class ArithmeticOperations {
	static Logger log = LogManager.getLogger("com.maiya");
	static String s_clog = " Arithmetic_Operations: ";
	private BigDecimal d_number1;
	private BigDecimal d_number2;
	private String s_express = null;
	private Expression expression;
	private BigDecimal bd_result;
	private BigDecimal i_result;
	private String s_result;
	private int i_precision;
	private String s_roundingMode;
	private MathContext m_mathContext=new MathContext(3, RoundingMode.HALF_UP);
	
	public enum MRoundingMode{
		//Rounding mode to round towards positive infinity. 
		//������������� 
		ROUND_CEILING(RoundingMode.CEILING),
		
		//Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up. 
		//�򣨾��룩�����һ�����룬�������ߣ��ľ��룩�����,�������������������, 1.55����һλС�����Ϊ1.6 
		ROUND_HALF_UP(RoundingMode.HALF_UP),
		
		//Rounding mode to round towards zero. 
		//���㷽������ 
		ROUND_DOWN(RoundingMode.DOWN),
		
		//Rounding mode to round towards negative infinity.
		//����������� 
		ROUND_FLOOR(RoundingMode.FLOOR),
		
		//Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round down. 
		//�򣨾��룩�����һ�����룬�������ߣ��ľ��룩�����,�������������������, ����1.55 ����һλС�����Ϊ1.5 
		ROUND_HALF_DOWN(RoundingMode.HALF_DOWN),
		
		//Rounding mode to round towards the "nearest neighbor" unless both neighbors are equidistant, in which case, round towards the even neighbor. 
		//�򣨾��룩�����һ�����룬�������ߣ��ľ��룩�����,������������������λ����������ʹ��ROUND_HALF_UP �������ż����ʹ��ROUND_HALF_DOWN 
		ROUND_HALF_EVEN(RoundingMode.HALF_EVEN), 
		
		//Rounding mode to assert that the requested operation has an exact result, hence no rounding is necessary. 
		//�������Ǿ�ȷ�ģ�����Ҫ����ģʽ 
		ROUND_UNNECESSARY(RoundingMode.UNNECESSARY),
		
		//Rounding mode to round away from zero. 
		//��Զ��0�ķ�������
		ROUND_UP(RoundingMode.UP);
		
		private RoundingMode rm=null;
		
		private MRoundingMode(RoundingMode rm){
			this.rm=rm;
		}
		
		public RoundingMode getRoundingMode(){
			return this.rm;
		}
		
	}

	public ArithmeticOperations() {
		log.info("######"+s_clog+"######"+" Create a Arithmetic_Operations");
	}

	public ArithmeticOperations(String precision, String roundingMode) {
		log.info("######"+s_clog+"######"+" Create a Arithmetic_Operations");
		this.s_roundingMode = roundingMode;
		this.i_precision = Integer.parseInt(precision);

		this.m_mathContext=new MathContext(this.i_precision,
				this.getRoundingMode(Integer.parseInt(this.s_roundingMode)));
		log.debug(s_clog+" the precision is "+ precision + " the roundingMode is "+this.getRoundingMode(Integer.parseInt(this.s_roundingMode)).toString());

		/*if (roundingMode.equals("1")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		} else if (roundingMode.equals("0")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.CEILING);
		} else {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		}
*/
	}

	public ArithmeticOperations(String s_express) {
		log.info("######"+s_clog+"######"+" Create a Arithmetic_Operations");
		this.expression = new Expression(s_express);
		log.debug(s_clog+" the express is "+ s_express );
	}

	public ArithmeticOperations(String s_express, String precision, String roundingMode) {
		log.info("######"+s_clog+"######"+" Create a Arithmetic_Operations");
		this.expression = new Expression(s_express,
				new MathContext(Integer.parseInt(precision),
						this.getRoundingMode(Integer.parseInt(roundingMode))));
		log.debug(s_clog+" the express is "+ s_express+" the precision is "
						+ precision + " the roundingMode is "+this.getRoundingMode(Integer.parseInt(roundingMode)).toString());
		
		/*if (roundingMode.equals("1")) {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.HALF_UP));
		} else if (roundingMode.equals("0")) {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.CEILING));
		} else {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.HALF_UP));
		}*/

	}

	public String doAddition(String s_number1, String s_number2) {
		log.debug(s_clog+" do addition "+ s_number1 +" and "+s_number2);
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.i_result = this.d_number1.add(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return this.s_result;
	}

	public String doSubtraction(String s_number1, String s_number2) {
		log.debug(s_clog+" do Subtraction "+ s_number1 +" and "+s_number2);
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.i_result = this.d_number1.subtract(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return s_result;
	}

	
	
	public String doDivision(String s_number1, String s_number2) {
		log.debug(s_clog+" do Division "+ s_number1 +" and "+s_number2);
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.i_result = this.d_number1.divide(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return s_result;
	}
	
	public String doDivision(String s_number1, String s_number2,String precision, String roundingMode) {
		log.debug(s_clog+" do Division "+ s_number1 +" and "+s_number2);
		log.debug(s_clog+" the precision is "
				+ precision + " the roundingModen is "+this.getRoundingMode(Integer.parseInt(roundingMode)).toString());
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.m_mathContext=new MathContext(this.i_precision,
				this.getRoundingMode(Integer.parseInt(roundingMode)));
		/*if (roundingMode.equals("1")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		} else if (roundingMode.equals("0")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.CEILING);
		} else {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		}*/
		this.i_result = this.d_number1.divide(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return s_result;
	}

	public String doMultiplication(String s_number1, String s_number2) {
		log.debug(s_clog+" do Multiplication "+ s_number1 +" and "+s_number2);
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.i_result = this.d_number1.multiply(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return s_result;
	}
	
	public String doMultiplication(String s_number1, String s_number2,String precision, String roundingMode) {
		log.debug(s_clog+" do Multiplication "+ s_number1 +" and "+s_number2);
		log.debug(s_clog+" the precision is "
				+ precision + " the roundingModen is "+this.getRoundingMode(Integer.parseInt(roundingMode)).toString());
		this.d_number1 = new BigDecimal(s_number1);
		this.d_number2 = new BigDecimal(s_number2);
		this.m_mathContext=new MathContext(this.i_precision,
				this.getRoundingMode(Integer.parseInt(roundingMode)));
		/*if (roundingMode.equals("1")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		} else if (roundingMode.equals("0")) {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.CEILING);
		} else {
			this.m_mathContext = new MathContext(this.i_precision, RoundingMode.HALF_UP);
		}*/
		this.i_result = this.d_number1.multiply(this.d_number2, this.m_mathContext);
		this.s_result = String.valueOf(this.i_result);
		log.debug(s_clog+" the result is "+ this.s_result);
		return s_result;
	}

	/*
	 * public String doMod(){ this.i_result=this.d_number1 % this.d_number2;
	 * this.s_result=String.valueOf(this.i_result); return s_result; }
	 */

	public String doExpression() {
		log.debug(s_clog+" do Expression ");
		this.bd_result = this.expression.eval();
		this.s_result = this.bd_result.toPlainString();
		log.debug(s_clog+" the result is "+ this.s_result);
		return this.s_result;

	}

	public String doExpression(String s_express, String precision, String roundingMode) {
		log.debug(s_clog+" do Expression and the exprssion is " +s_express);
		log.debug(s_clog+" the precision is "
				+ precision + " the roundingModen is "+this.getRoundingMode(Integer.parseInt(roundingMode)).toString());
		
		this.expression = new Expression(s_express,
				new MathContext(Integer.parseInt(precision),
						this.getRoundingMode(Integer.parseInt(roundingMode))));
		/*if (roundingMode.equals("1")) {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.HALF_UP));
		} else if (roundingMode.equals("0")) {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.CEILING));
		} else {
			this.expression = new Expression(s_express,
					new MathContext(Integer.parseInt(precision), RoundingMode.HALF_UP));
		}*/
		// this.expression.setPrecision(Integer.parseInt(s_precision));
		this.bd_result = this.expression.eval();
		//this.s_result = String.valueOf(this.bd_result);
		this.s_result = this.bd_result.toPlainString();
		log.debug(s_clog+" the result is "+ this.s_result);
		return this.s_result;
		//return new BigDecimal(this.s_result.toCharArray()).stripTrailingZeros().toPlainString();
		//return new BigDecimal(this.s_result.toCharArray()).toPlainString();
	}
	
	private RoundingMode getRoundingMode(int RoundingMode){
		RoundingMode rm = null;
		switch(RoundingMode){
		case 0:
			rm=MRoundingMode.valueOf("ROUND_CEILING").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 1:
			rm=MRoundingMode.valueOf("ROUND_HALF_UP").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 2:
			rm=MRoundingMode.valueOf("ROUND_DOWN").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 3:
			rm=MRoundingMode.valueOf("ROUND_FLOOR").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 4:
			rm=MRoundingMode.valueOf("ROUND_HALF_DOWN").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 5:
			rm=MRoundingMode.valueOf("ROUND_HALF_EVEN").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 6:
			rm=MRoundingMode.valueOf("ROUND_UNNECESSARY").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		case 7:
			rm=MRoundingMode.valueOf("ROUND_UP").getRoundingMode();
			log.debug("the RoundingMode selected is "+rm.toString());
			break;
		default:
			log.debug("the RoundingMode value should be in 1-7, now it will use default mode ROUND_CEILING");
			rm=MRoundingMode.valueOf("ROUND_CEILING").getRoundingMode();
			break;
		}
		return rm;	
	}
	
	public boolean plog(String s) {
		log.info(s);
		return true;
	}

}


