import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class Test extends JFrame {

	static int sizeString;
	static int[][] matrix;
	static int justFree;

	static int Prec(char ch) { // приоритетность операций
		switch (ch) {
		case '>':
		case '=':
			return 1;
		case '+':
			return 2;
		case '&':
			return 3;
		case '!':
			return 4;
		}
		return -1;
	}

	static String infixToPostfix(String exp) {
		String result = new String("");
		Stack<Character> stack = new Stack<>();

		for (int i = 0; i < exp.length(); ++i) {
			char c = exp.charAt(i); // считываем посимвольно

			if (Character.isLetter(c))
				result += c;
			else if (c == '(')
				stack.push(c);
			else if (c == ')') {
				while (!stack.isEmpty() && stack.peek() != '(')
					result += stack.pop(); // достаем из стека удаляя

				if (!stack.isEmpty() && stack.peek() != '(')
					return "Invalid Expression";
				else
					stack.pop();

			} else { // если это оператор
				while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek()))
					result += stack.pop();
				stack.push(c);
			}
		}
		while (!stack.isEmpty()) // извлеките все операторы из стека
			result += stack.pop();

		return result; // выражение в постфиксной форме
	}

	static void getInfix(String exp2) {
		ArrayList<String> columnNames = new ArrayList<>();
		Set<String> perem = new HashSet<>();
		Stack<String> stack = new Stack<>();
		String op1 = new String("");
		String op2 = new String("");
		int kolvoDoing = 0;
		
		for (int i = 0; i < exp2.length(); ++i) {
			char c = exp2.charAt(i);
			if (Character.isLetter(c))
				perem.add(Character.toString(c)); // начальные переменные
			else
				kolvoDoing++;
		}
		
		columnNames.addAll(perem);
		int kolvoPerem = columnNames.size(); // количество переменных
		int sizeColumns = kolvoDoing + kolvoPerem;
		sizeString = (int) Math.pow(2, kolvoPerem);
		matrix = new int[sizeString][sizeColumns];
		justFree = kolvoPerem;
		int h = 0;
		while (h < kolvoPerem - 1) {
			for (int j = kolvoPerem - 1; j >= 0; j--) {
				for (int i = 0; i < sizeString; i++) {
					matrix[i][h] = (i / (int) Math.pow(2, j)) % 2;
				}
				h++;
			}
		}
		int index;
		int index2;
		for (int i = 0; i < exp2.length(); ++i) {
			char c = exp2.charAt(i);
			if (Character.isLetter(c)) {
				stack.push(Character.toString(c));
			} else {
				op1 = stack.pop();
				index = columnNames.indexOf(op1);
				if (stack.isEmpty() || c == '!') {
					columnNames.add((c + op1));
					stack.push(c + op1);
					Schet(c, index, 0);
				} else {
					op2 = stack.pop();
					index2 = columnNames.indexOf(op2);
					columnNames.add("(" + op2 + c + op1 + ")");
					stack.push("(" + op2 + c + op1 + ")");
					Schet(c, index, index2);
				}
			}
		}

		String[] str = (String[]) columnNames.toArray(new String[] {});
		String[][] matrix2 = new String[sizeString][justFree];
		for (int j = 0; j < justFree; j++) {
			for (int i = 0; i < sizeString; i++) {
				matrix2[i][j] = Integer.toString(matrix[i][j]);
			}
		}
		Test(matrix2, str);
	}

	private static byte Schet(char c, int index, int index2) {

		switch (c) {
		case '+':
			for (int i = 0; i < sizeString; i++) {
				matrix[i][justFree] = matrix[i][index] | matrix[i][index2];
			}
			justFree++;
			break;
		case '&':
			for (int i = 0; i < sizeString; i++) {
				matrix[i][justFree] = matrix[i][index] & matrix[i][index2];
			}
			justFree++;
			break;
		case '>':
			for (int i = 0; i < sizeString; i++) {
				if (matrix[i][index] == matrix[i][index2])
					matrix[i][justFree] = 1;
				else if (matrix[i][index2] == 0)
					matrix[i][justFree] = 1;
				else
					matrix[i][justFree] = 0;
			}
			justFree++;
			break;
		case '=':
			for (int i = 0; i < sizeString; i++) {
				if (matrix[i][index] == matrix[i][index2])
					matrix[i][justFree] = 1;
				else
					matrix[i][justFree] = 0;
			}
			justFree++;
			break;
		case '!':
			for (int i = 0; i < sizeString; i++) {
				if (matrix[i][index] == 0)
					matrix[i][justFree] = 1;
				else
					matrix[i][justFree] = 0;
			}
			justFree++;
			break;

		}
		return -1;
	}

	public static void Test(String[][] matrix, String[] eval) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTable table = new JTable(matrix, eval);
		JScrollPane scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		String exp = "(c+a&b)>!c";
		String ex2 = infixToPostfix(exp);
		getInfix(ex2);
	}
}
//a+b&!(c+a=b)+(c+a&b)>!c
//a=b>!c
//a=b>!c+g
