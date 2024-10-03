import java.math.BigDecimal;
import java.math.RoundingMode;

public class MOB {

    public static BigDecimal[][] getDeltaY(BigDecimal[][] Y, BigDecimal[][] deltYj) {
        BigDecimal[][] result = new BigDecimal[3][1];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1; j++) {
                result[i][j] = Y[i][j].multiply(deltYj[i][j]);
            }
        }
        return result;
    }

    public static BigDecimal[][] zeroMatrixSubstruction(BigDecimal[][] matrix, int f) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        BigDecimal[][] result = new BigDecimal[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (f == 0) {
                    if (i == j) {
                        result[i][j] = BigDecimal.ONE.subtract(matrix[i][j]);
                    } else {
                        result[i][j] = BigDecimal.ZERO.subtract(matrix[i][j]);
                    }
                }
                if (f == 1) {
                    if (i == j) {
                        result[i][j] = matrix[i][j].subtract(BigDecimal.ONE);
                    } else {
                        result[i][j] = matrix[i][j].subtract(BigDecimal.ZERO);
                    }
                }

            }
        }
        return result;
    }

    //Вспомогательно для нахождения алгебраического дополнения
    public static BigDecimal[][] getSubmatrix(BigDecimal[][] matrix, int rowToRemove, int colToRemove) {
        int n = matrix.length;
        BigDecimal[][] submatrix = new BigDecimal[n - 1][n - 1];

        int rowCount = 0;
        int colCount = 0;
        for (int i = 0; i < n; i++) {
            if (i != rowToRemove) {
                for (int j = 0; j < n; j++) {
                    if (j != colToRemove) {
                        submatrix[rowCount][colCount] = matrix[i][j];
                        colCount++;
                    }
                }
                rowCount++;
            }
            colCount = 0;
        }
        return submatrix;
    }

    public static BigDecimal getDeterminant(BigDecimal[][] matrix) {
        int n = matrix.length;
        BigDecimal determinant = BigDecimal.ZERO;
        if (n == 2) {
            determinant = matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        } else {

            for (int i = 0; i < n; i++) {
                BigDecimal mainDiag = matrix[0][0].multiply(matrix[1][1]).multiply(matrix[2][2]);
                BigDecimal leftDownTriangle = matrix[1][0].multiply(matrix[2][1]).multiply(matrix[0][2]);
                BigDecimal rightUpTriangle = matrix[2][0].multiply(matrix[0][1]).multiply(matrix[1][2]);
                BigDecimal sum = mainDiag.add(leftDownTriangle).add(rightUpTriangle);

                BigDecimal sideDiag = matrix[0][2].multiply(matrix[1][1]).multiply(matrix[2][0]);
                BigDecimal leftUpTriangle = matrix[1][0].multiply(matrix[0][1]).multiply(matrix[2][2]);
                BigDecimal rightDownTriangle = matrix[0][0].multiply(matrix[2][1]).multiply(matrix[1][2]);
                BigDecimal div = sideDiag.add(leftUpTriangle).add(rightDownTriangle);

                determinant = sum.subtract(div);
            }
        }
        return determinant;
    }

    public static BigDecimal[][] algebraicComplement(BigDecimal[][] EdivA) {
        int rows = EdivA.length;
        int cols = EdivA[0].length;
        BigDecimal[][] Xij = new BigDecimal[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BigDecimal[][] submatrix = getSubmatrix(EdivA, i, j);
                BigDecimal minor = getDeterminant(submatrix);
                Xij[i][j] = minor.multiply(BigDecimal.valueOf(Math.pow(-1, i + j)));
            }
        }
        return Xij;
    }

    public static BigDecimal[][] inversingMatrix(BigDecimal[][] matrix, BigDecimal determinant) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        BigDecimal[][] transposedMatrix = new BigDecimal[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        BigDecimal[][] inverseMatrix = new BigDecimal[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                inverseMatrix[i][j] = transposedMatrix[i][j].divide(determinant, 6, RoundingMode.HALF_UP);
            }
        }
        return inverseMatrix;
    }

    public static BigDecimal[][] multiplyMatrices(BigDecimal[][] matrix1, BigDecimal[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        BigDecimal[][] result = new BigDecimal[rows1][cols2];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                result[i][j] = BigDecimal.ZERO;
                for (int k = 0; k < cols1; k++) {
                    result[i][j] = result[i][j].add(matrix1[i][k].multiply(matrix2[k][j]));
                }
            }
        }
        return result;
    }

    //Определение изменений межотраслевых потоков
    public static BigDecimal[][] interFlow(BigDecimal[][] matrixA, BigDecimal[][] deltX) {
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        BigDecimal[][] deltaXij = new BigDecimal[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                deltaXij[i][j] = matrixA[i][j].multiply(deltX[j][0]);
            }
        }
        return deltaXij;
    }

    //Для подсчета изменений потребности в ОПФ и трудовых ресурсах, а также при сложении для величины чистого продукта
    public static BigDecimal[][] singlColumnAction(BigDecimal[][] matrix1, BigDecimal[][] matrix2, int f) {
        int rows = matrix2.length;
        int cols = matrix2[0].length;
        BigDecimal[][] result = new BigDecimal[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (f == 0) result[i][0] = matrix1[i][0].multiply(matrix2[i][0]);
            if (f == 1) result[i][0] = matrix1[i][0].subtract(matrix2[i][0]);
            if (f == 2) result[i][0] = matrix1[i][0].add(matrix2[i][0]);
        }
        return result;
    }

    public static BigDecimal[][] sumElementInRow(BigDecimal[][] Xij) {
        int rows = Xij.length;
        int cols = Xij[0].length;

        BigDecimal[][] result = new BigDecimal[3][1];
        for (int i = 0; i < rows; i++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = 0; j < cols; j++) {
                sum = sum.add(Xij[i][j]);
            }
            result[i][0] = sum;
        }
        return result;
    }

    public static BigDecimal[][] sumElementInColumn(BigDecimal[][] Xij) {
        int rows = Xij.length;
        int cols = Xij[0].length;

        BigDecimal[][] result = new BigDecimal[3][1];
        for (int j = 0; j < cols; j++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < rows; i++) {
                sum = sum.add(Xij[i][j]);
            }
            result[j][0] = sum;
        }
        return result;
    }

    public static void printMatrix(BigDecimal[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("В этой задаче заданы: ");
        int n = 3;
        System.out.println();
        System.out.println("Матрица коэффициентов прямых материальных затрат:");

        BigDecimal[][] matrixA = new BigDecimal[n][n];
        matrixA[0][0] = BigDecimal.valueOf(0.4);
        matrixA[0][1] = BigDecimal.valueOf(0.1);
        matrixA[0][2] = BigDecimal.valueOf(0.3);
        matrixA[1][0] = BigDecimal.valueOf(0.2);
        matrixA[1][1] = BigDecimal.valueOf(0.3);
        matrixA[1][2] = BigDecimal.valueOf(0.3);
        matrixA[2][0] = BigDecimal.valueOf(0.3);
        matrixA[2][1] = BigDecimal.valueOf(0.2);
        matrixA[2][2] = BigDecimal.valueOf(0.2);
        printMatrix(matrixA);


        System.out.println();
        System.out.println("Вектор конечного продукта Y: ");

        BigDecimal[][] vectY = new BigDecimal[n][1];
        vectY[0][0] = BigDecimal.valueOf(30.0);
        vectY[1][0] = BigDecimal.valueOf(30.0);
        vectY[2][0] = BigDecimal.valueOf(85.0);
        printMatrix(vectY);

        System.out.println();
        System.out.println("Вектор изменения валового продукта deltY: ");

        BigDecimal[][] deltaY = new BigDecimal[n][1];
        deltaY[0][0] = BigDecimal.valueOf(0.2);
        deltaY[1][0] = BigDecimal.valueOf(0.1);
        deltaY[2][0] = BigDecimal.valueOf(-0.2);

        BigDecimal[][] deltaVectY = getDeltaY(vectY, deltaY);
        printMatrix(deltaVectY);

        System.out.println();
        System.out.println("Коэффициенты прямой трудоемкости: ");
        BigDecimal[][] t = new BigDecimal[n][1];
        t[0][0] = BigDecimal.valueOf(1.5);
        t[1][0] = BigDecimal.valueOf(2.0);
        t[2][0] = BigDecimal.valueOf(3.0);
        printMatrix(t);

        System.out.println();
        System.out.println("Коэффициенты прямых затрат ОПФ: ");
        BigDecimal[][] f = new BigDecimal[n][1];
        f[0][0] = BigDecimal.valueOf(4.0);
        f[1][0] = BigDecimal.valueOf(3.5);
        f[2][0] = BigDecimal.valueOf(2.0);
        printMatrix(f);

        System.out.println();
        System.out.println("1 - Изменение валового выпуска: ");
        System.out.println("Сначала вычислим E-A:");
        BigDecimal[][] matrixEdivA = zeroMatrixSubstruction(matrixA, 0);
        printMatrix(matrixEdivA);

        System.out.println();
        System.out.println("Далее вычислим алгебраическое дополнение ");
        BigDecimal[][] Dij = algebraicComplement(matrixEdivA);
        printMatrix(Dij);

        System.out.println();
        System.out.println("Также вычислим определитель матрицы E-A: ");
        BigDecimal detEdivA = getDeterminant(matrixEdivA);
        System.out.println(detEdivA);

        System.out.println();
        System.out.println("Вычислим обратную матрицу (E-A)^-1: ");
        BigDecimal[][] inversEdivA = inversingMatrix(Dij, detEdivA);
        printMatrix(inversEdivA);

        System.out.println();
        System.out.println("Изменение валового выпуска deltX= ((E-A)^-1)*deltY: ");
        BigDecimal[][] deltX = multiplyMatrices(inversEdivA, deltaVectY);
        printMatrix(deltX);

        System.out.println();
        System.out.println("2 - Матрица изменений межотраслевых потоков deltXij= aij*deltXj: ");
        printMatrix(interFlow(matrixA, deltX));

        System.out.println();
        System.out.println("3 - Изменение потребности в трудовых ресурсах delt(Xj)t =tj*deltXj: ");
        printMatrix(singlColumnAction(t, deltX, 0));

        System.out.println();
        System.out.println("4 - Изменение потребности в ОПФ deltFj=fi*deltX: ");
        printMatrix(singlColumnAction(f, deltX, 0));

        System.out.println();
        System.out.println("5 - Объем валового выпуска X= ((E-A)^-1)*Y: ");
        BigDecimal[][] X = multiplyMatrices(inversEdivA, vectY);
        printMatrix(X);

        System.out.println();
        System.out.println("6 - Промежуточный продукт: ");
        System.out.println("Матрица межотраслевых потоков Xij: ");
        BigDecimal[][] Xij = interFlow(matrixA, X);
        printMatrix(Xij);

        System.out.println();
        System.out.println("Промежуточный продукт по отраслям (сумма элементов в Xij по строкам): ");
        printMatrix(sumElementInRow(Xij));

        System.out.println();
        System.out.println("7 - Материальные затраты (сумма элементов в Xij по столбцам) : ");
        printMatrix(sumElementInColumn(Xij));

        System.out.println();
        System.out.println("8 - Условно-чистый продукт Zj=Xj-Sum(i=1,n)Xij: ");
        BigDecimal[][] Zi = singlColumnAction(X, sumElementInColumn(Xij), 1);
        printMatrix(Zi);

        System.out.println();
        System.out.println("9 - Коэффициенты косвенных материальных затрат 1-го порядка (А*А) : ");
        printMatrix(multiplyMatrices(matrixA, matrixA));

        System.out.println();
        System.out.println("10 - Коэффициенты полных материальных затрат С=(Е-А)^1-E : ");
        printMatrix(zeroMatrixSubstruction(inversEdivA, 1));

        System.out.println();
        System.out.println("11 - Совокупные затраты живого труда (Xj)t+delt(Xj)t=tj(Xj+deltXj): ");
        System.out.println("Сначала вычислим Xj+deltXj");
        BigDecimal[][] sumXjDeltX = singlColumnAction(deltX, X, 2);
        printMatrix(sumXjDeltX);
        System.out.println();
        System.out.println("Теперь выполним умножение на tj и получим совокупные затраты живого труда:");
        printMatrix(singlColumnAction(t, sumXjDeltX, 0));

        System.out.println();
        System.out.println("12 - Совокупные затраты ОПФ Фj=fj*Xj (Xj - объем производства валовой продукции): ");
        printMatrix(singlColumnAction(f, X, 0));
    }
}
