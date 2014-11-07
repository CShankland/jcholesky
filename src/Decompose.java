import java.io.*;

/**
 * This class when executed with the proper inputs should produce a solution, A'
 * to the equation A' = A - CD^-1C~
 * 
 * The method of this solution is by using a Cholesky decomposition to produce a
 * triangular matrix R such that D = RR~ and then produce a matrix F = CR^-1 so
 * that A' = A - FF~
 * 
 * Note: '~' means transpose.
 * 
 * @author christopher
 * 
 */
public class Decompose {
	private static final String MAIN_MENU = "\nMain Menu: \n\n"
		+ "1.  Enter the matrix that serves as A\n"
		+ "2.  Enter the matrix that serves as C\n"
		+ "3.  Enter the matrix that serves as D\n"
		+ "4.  Enter the output path for A'\n"
		+ "5.  Compute A'\n\n"
		+ "Note: To check current settings, enter the relavent choice and select 'See Current'\n"
		+ "\nEnter a selection or 'exit' to quit: ";
	private static final String INPUT_A_MENU = "\nPlease enter the dimensions of A in the format 'rows columns'"
		+ " and then the path of the data file for A or 'c' to display "
		+ "the current data:";
	private static final String INPUT_C_MENU = "\nPlease enter the dimensions of C in the format 'rows columns'"
		+ " and then the path of the data file for C or 'c' to display "
		+ "the current data:";
	private static final String INPUT_D_MENU = "\nPlease enter the dimensions of D in the format 'rows columns'"
		+ " and then the path of the data file for D or 'c' to display "
		+ "the current data:";
	private static final String CURRENT_SETTINGS_A = "\nCurrent values for A are: ";
	private static final String CURRENT_SETTINGS_C = "\nCurrent values for C are: ";
	private static final String CURRENT_SETTINGS_D = "\nCurrent values for D are: ";
	private static final String PATH_INPUT_A_PRIME = "\nPlease enter the name of the output file that you would like "
		+ "the matrix A' written to or 'c' to display the current path:";
	private static final String CURRENT_SETTING_PATH = "\nThe current output path is: ";

	static String choice;
	static String outputPath;

	static FileInputStream fis;
	static BufferedInputStream bis;
	static DataInputStream dis;
	static BufferedReader buff;

	static File dataA;
	static File dataC;
	static File dataD;
	static File output;

	static Matrix A;
	static Matrix C;
	static Matrix D;

	public static void main(String[] args) {
		System.out.print(MAIN_MENU);
		buff = new BufferedReader(new InputStreamReader(System.in));

		A = null;
		C = null;
		D = null;

		try {
			choice = buff.readLine();
			if (choice.equalsIgnoreCase("exit")) {
				shutdown();
			}
			switch (Integer.parseInt(choice)) {
			case 1:
				inputA();
			case 2:
				inputC();
			case 3:
				inputD();
			case 4:
				pathAPrime();
			case 5:
				compute();
			default:
				mainMenu();
			}
		} catch (IOException ioe) {
			System.err.println("Error reading first choice.  Exiting.");
			System.exit(0);
		}
	}

	private static void inputA() {
		System.out.print(INPUT_A_MENU);

		try {
			choice = buff.readLine();
			if (choice.equals("c")) {
				System.out.print(CURRENT_SETTINGS_A + "\nRows: " + A.ROWS
						+ " Columns: " + A.COLUMNS + "\n" + A.toString());
				mainMenu();
			} else {
				String[] args = choice.split(" ");
				A = new Matrix(Integer.parseInt(args[0]), Integer
						.parseInt(args[1]));
				dataA = new File(args[2]);
			}
		} catch (IOException ioe) {
			System.err.println("Error parsing input.  Returning to main menu");
			mainMenu();
		} catch (Exception e) {
			System.err.println("Really bad error.  Quitting.");
			System.exit(0);
		}

		try {
			fis = new FileInputStream(dataA);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			int l = 0;
			while (dis.available() != 0) {
				String line = dis.readLine();
				String[] tokens = line.split(" ");
				float[] matLine = new float[tokens.length];
				for (int i = 0; i < matLine.length; i++) {
					matLine[i] = Float.parseFloat(tokens[i]);
				}
				A.setRow(l, matLine);
				l++;
			}
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException ioe) {
			System.err.println("Error reading A data file.");
			mainMenu();
		}

		mainMenu();
	}

	private static void inputC() {
		System.out.print(INPUT_C_MENU);

		try {
			choice = buff.readLine();
			if (choice.equals("c")) {
				System.out.print(CURRENT_SETTINGS_C + "\nRows: " + C.ROWS
						+ " Columns: " + C.COLUMNS + "\n" + C.toString());
				mainMenu();
			} else {
				String[] args = choice.split(" ");
				C = new Matrix(Integer.parseInt(args[0]), Integer
						.parseInt(args[1]));
				dataC = new File(args[2]);
			}
		} catch (IOException ioe) {
			System.err.println("Error parsing input.  Returning to main menu");
			mainMenu();
		} catch (Exception e) {
			System.err.println("Really bad error.  Quitting.");
			System.exit(0);
		}

		try {
			fis = new FileInputStream(dataC);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			int l = 0;
			while (dis.available() != 0) {
				String line = dis.readLine();
				String[] tokens = line.split(" ");
				float[] matLine = new float[tokens.length];
				for (int i = 0; i < matLine.length; i++) {
					matLine[i] = Float.parseFloat(tokens[i]);
				}
				C.setRow(l, matLine);
				l++;
			}
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException ioe) {
			System.err.println("Error reading C data file.");
			mainMenu();
		}

		mainMenu();
	}

	private static void pathAPrime() {
		System.out.print(PATH_INPUT_A_PRIME);

		try {
			choice = buff.readLine();
			if (choice.equals("c")) {
				System.out.print(CURRENT_SETTING_PATH + outputPath);
				mainMenu();
			} else {
				outputPath = choice;
				output = new File(outputPath);
				output.createNewFile();
			}
		} catch (IOException ioe) {
			System.err.println("Error parsing input.  Returning to main menu");
			mainMenu();
		} catch (Exception e) {
			System.err.println("Really bad error.  Quitting.");
			System.exit(0);
		}

		mainMenu();
	}

	private static void mainMenu() {
		System.out.print(MAIN_MENU);
		buff = new BufferedReader(new InputStreamReader(System.in));

		try {
			choice = buff.readLine();

			if (choice.equalsIgnoreCase("exit")) {
				shutdown();
			}

			switch (Integer.parseInt(choice)) {
			case 1:
				inputA();
			case 2:
				inputC();
			case 3:
				inputD();
			case 4:
				pathAPrime();
			case 5:
				compute();
			default: {
				System.out.println("Invalid input");
				mainMenu();
			}
			}

		} catch (IOException ioe) {
			System.err.println("Error reading choice.  Exiting.");
			System.exit(0);
		}
	}

	private static void inputD() {
		System.out.print(INPUT_D_MENU);
		try {
			choice = buff.readLine();
			if (choice.equals("c")) {
				System.out.print(CURRENT_SETTINGS_D + "\nRows: " + D.ROWS
						+ " Columns: " + D.COLUMNS + "\n" + D.toString());
				mainMenu();
			} else {
				String[] args = choice.split(" ");
				D = new Matrix(Integer.parseInt(args[0]), Integer
						.parseInt(args[1]));
				dataD = new File(args[2]);
			}
		} catch (IOException ioe) {
			System.err.println("Error parsing input.  Returning to main menu");
			mainMenu();
		} catch (Exception e) {
			System.err.println("Really bad error.  Quitting.");
			System.exit(0);
		}

		try {
			fis = new FileInputStream(dataD);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			int l = 0;
			while (dis.available() != 0) {
				String line = dis.readLine();
				String[] tokens = line.split(" ");
				float[] matLine = new float[tokens.length];
				for (int i = 0; i < matLine.length; i++) {
					matLine[i] = Float.parseFloat(tokens[i]);
				}
				D.setRow(l, matLine);
				l++;
			}
			dis.close();
			bis.close();
			fis.close();
		} catch (IOException ioe) {
			System.err.println("Error reading D data file.");
			mainMenu();
		}

		mainMenu();
	}

	private static void compute() {
		if (A != null && C != null && D != null && output.isFile()
				&& output.canWrite()) {

			Matrix R = choleskyDecomposition(D);
			//System.out.println("*****R*****\n" + R.toString());
			R.invert();
			//System.out.println("*****R INVERSE*****\n" + R.toString());
			Matrix F = Matrix.mul(C, R);
			//System.out.println("*****F*****\n" + F.toString());
			Matrix sol = A.sub(Matrix.mul(F, F.transpose2()));
			//System.out.println("*****SOLUTION*****\n" + sol.toString());

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						output));
				for (int i = 0; i < sol.ROWS; i++) {
					for (int j = 0; j < sol.COLUMNS - 1; j++) {
						writer.write((sol.get(i, j) + " "));
					}
					writer.write(sol.get(i,sol.COLUMNS-1) + "");
					if (i != sol.ROWS - 1)
						writer.newLine();
				}
				writer.flush();
				writer.close();
				System.out.println("File written.");
			} catch (IOException ioe) {
				System.out.println("Problem writing file.  Displaying output to screen.\n");
				System.out.println(sol.toString());
			}
			mainMenu();
		} else {
			System.err.println("All variables not set, or output file is not available for writing.");
			mainMenu();
		}

		System.out.println("You should never see this.");
		mainMenu();
	}

	private static Matrix choleskyDecomposition(Matrix a) {
		Matrix l = new Matrix(a.ROWS, a.COLUMNS);
		l.setElement(0, 0, (float) Math.sqrt(a.get(0, 0)));
		for (int i = 0; i < a.ROWS; i++) {
			float sum1 = 0;
			for (int j = 0; j < i; j++) {
				float sum2 = 0;
				for (int k = 0; k < j; k++) {
					// System.out.println("LOWER Sum: " + sum2 + "  i,j : " + i
					// + "," + j);
					sum2 += l.get(i, k) * l.get(j, k);
				}
				// System.out.println("First bit: " + (1/l.get(j,j)) +
				// "  Second bit: " + (a.get(i,j) - sum2));
				// System.out.println("Setting element " + i + "," + j + " to "
				// + (1/l.get(j,j)) * (a.get(i,j) - sum2));
				l.setElement(i, j, (1 / l.get(j, j)) * (a.get(i, j) - sum2));
			}
			for (int k = 0; k < i; k++) {
				// System.out.println("DIAGONAL Sum: " + sum1 + "  i,k : " + i +
				// "," + k + "  Value: " + l.get(i, k));
				sum1 += l.get(i, k) * l.get(i, k);
			}
			// System.out.println("Setting element " + i + "," + i + " to " +
			// (float)Math.sqrt(a.get(i, i) - sum1));
			l.setElement(i, i, (float) Math.sqrt(a.get(i, i) - sum1));
		}
		return l;
	}

	private static void shutdown() {
		if (bis != null) {
			try {
				bis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		if (dis != null) {
			try {
				dis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		if (fis != null) {
			try {
				fis.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		System.exit(0);
	}
}
