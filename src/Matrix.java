public class Matrix {
	private static final int NOT_TRIANGULAR = -1;
	private static final int UPPER_TRIANGULAR = 1;
	private static final int LOWER_TRIANGULAR = 2;

	public final int ROWS;
	public final int COLUMNS;

	private boolean isSquare = false;

	private float[][] data;

	public Matrix(float[][] data) {
		ROWS = data.length;
		COLUMNS = data[0].length;
		this.data = data;
		if (ROWS == COLUMNS)
			isSquare = true;
	}

	public Matrix(int rows, int columns) {
		ROWS = rows;
		COLUMNS = columns;
		if (columns == rows)
			isSquare = true;
		data = new float[rows][columns];
	}

	public Matrix(int rows, int columns, float[][] data) {
		ROWS = rows;
		COLUMNS = columns;
		if (columns == rows)
			isSquare = true;
		this.data = data;
	}

	public void setElement(int row, int column, float value) {
		data[row][column] = value;
	}

	public void setRow(int row, float[] values) {
		for (int i = 0; i < COLUMNS; i++) {
			data[row][i] = values[i];
		}
	}

	public void setColumn(int column, float[] values) {
		for (int i = 0; i < ROWS; i++) {
			data[i][column] = values[i];
		}
	}

	public void scaleRow(float[][] input, int row, float value) {
		for (int i = 0; i < COLUMNS; i++) {
			// System.out.print("SCALE  Old row value: " + input[row][i]);
			input[row][i] *= value;
			// System.out.println(".  New row value: " + input[row][i] + ".");
		}
	}

	public void scaleColumn(float[][] input, int column, float value) {
		for (int i = 0; i < ROWS; i++) {
			input[i][column] *= value;
		}
	}

	public void addRow(float[][] input, float[] row, float scale, int rowNum) {
		for (int i = 0; i < input[rowNum].length; i++) {
			// System.out.print("ADD  Old row value: " + input[rowNum][i]);
			input[rowNum][i] += row[i] * scale;
			// System.out.println(".  New row value: " + input[rowNum][i] +
			// ".");
		}
	}

	public void transpose() {
		float[][] temp = new float[COLUMNS][ROWS];

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				temp[j][i] = data[i][j];
			}
		}
		data = temp;
	}

	public Matrix transpose2() {
		float[][] temp = new float[COLUMNS][ROWS];

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				temp[j][i] = data[i][j];
			}
		}
		return new Matrix(temp);
	}

	public void invert() {
		if (isSquare) {
			invertSquare();
		} else {
			System.out.println("Non-square matrices don't get to be inverted.");
		}
	}

	public Matrix sub(Matrix b) {
		float[][] temp = new float[ROWS][COLUMNS];

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				temp[i][j] = data[i][j] - b.get(i, j);
			}
		}

		return new Matrix(temp);
	}

	public Matrix add(Matrix b) {
		float[][] temp = new float[ROWS][COLUMNS];

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				temp[i][j] = data[i][j] + b.get(i, j);
			}
		}

		return new Matrix(temp);
	}

	public float get(int i, int j) {
		return data[i][j];
	}

	public static Matrix mul(Matrix a, Matrix b) {
		Matrix ab = new Matrix(a.ROWS, b.COLUMNS);
		for (int i = 0; i < a.ROWS; i++) {
			for (int j = 0; j < b.COLUMNS; j++) {
				float val = 0;
				for (int k = 0; k < a.COLUMNS; k++) {
					val += a.get(i, k) * b.get(k, j);
				}
				ab.setElement(i, j, val);
			}
		}
		return ab;
	}

	private void invertSquare() {
		switch (checkTriangular()) {
		case UPPER_TRIANGULAR: {
			//System.out.println("Upper Triangular");
			transpose();
			invertTriangular();
			transpose();
			return;
		}
		case LOWER_TRIANGULAR: {
			//System.out.println("Lower Triangular");
			invertTriangular();
			return;
		}
		case NOT_TRIANGULAR: {
			System.out.println("Not Implemented");
			return;
		}
		default: {
			System.out.println("Invalid triangular case");
			return;
		}
		}
	}

	private int checkTriangular() {
		boolean upper = true;
		boolean lower = true;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (j > i && data[i][j] != 0)
					lower = false;
				if (i > j && data[i][j] != 0)
					upper = false;
			}

		}

		if (upper)
			return UPPER_TRIANGULAR;
		if (lower)
			return LOWER_TRIANGULAR;
		return NOT_TRIANGULAR;
	}

	private void invertTriangular() {
		float[][] inverse = new float[ROWS][COLUMNS];

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (i == j) {
					inverse[i][j] = 1;
				} else {
					inverse[i][j] = 0;
				}
			}
		}

		for (int i = 0; i < COLUMNS; i++) {
			float pivot = data[i][i];
			// System.out.println("Scaling row " + i + " in inverse by " +
			// 1f/pivot + ".");
			scaleRow(inverse, i, 1f / pivot);
			for (int j = i + 1; j < ROWS; j++) {
				// System.out.println("Adding row " + i + " with scale factor "
				// + -data[i][j] + " to row " + j + ".");
				addRow(inverse, inverse[i], -data[j][i], j);
			}
		}

		data = inverse;
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS - 1; j++) {
				output = output + data[i][j] + " ";
			}
			output = output + data[i][COLUMNS - 1] + "\n";
		}
		return output;
	}
}
