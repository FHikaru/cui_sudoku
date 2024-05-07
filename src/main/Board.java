package main;

/**
 *　数独の盤面
 */
public class Board {
	public final int SIZE; //盤面の一辺の長さ[平方数]
	public final int SIZESQRT; //盤面の格子一辺の長さ[SIZEの平方根]
	private static final int INITNUM = -1; //盤面の初期値
	
	private int[][] cells; //盤面そのもの。値は[0, SIZE)
	private int[][] defaultCells; //盤面の初期値を記憶する(初期盤面と人の入力を区別する用)
	
	/**
	 * コンストラクタ
	 */
	public Board(int size) {
		//sizeが平方数かの判定
		int sizeSqrt = (int)Math.sqrt((double)size);
		if(size != sizeSqrt * sizeSqrt) {
			System.err.println("引数が平方数ではありません");
		}
		SIZE = size;
		SIZESQRT = sizeSqrt;
		cells = new int[SIZE][SIZE];
		//初期化
		for(int row = 0; row < SIZE; row++) {
			for(int column = 0; column < SIZE; column++) {
				cells[row][column] = INITNUM;
			}
		}
		registerCells();
	}
	
	/**
	 * コピーコンストラクタ
	 * Boardインスタンスで初期化する
	 * @param board　: Board : コピー元のBoard
	 */
	public Board(Board board) {
		this.SIZE = board.SIZE;
		this.SIZESQRT = board.SIZESQRT;
		this.cells = new int[board.SIZE][board.SIZE];
		for(int row = 0; row < board.SIZE; row++) {
			for(int column = 0; column < board.SIZE; column++) {
				cells[row][column] = board.cells[row][column];
			}
		}
		registerCells();
	}
	
	/**
	 * コピー生成
	 * @return　Board : コピー後のBoard
	 */
	public Board copyBoard() {
		return new Board(this);
	}
	
	/**
	 * 現盤面を問題盤面として登録する。
	 * 登録された盤面はdefaultCellsに格納される。
	 * 盤面は１つしか保存できないため、直近でregisterCellを実行したときの現盤面を保持する。
	 */
	public void registerCells() {
		defaultCells = new int[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				defaultCells[i][j] = cells[i][j];
			}
		}
	}
	
	/**
	 * 盤面のセル内に値を代入する
	 * @param rowIndex : 行のインデックス　[1, 盤面の一辺の長さ]
	 * @param colIndex : 列のインデックス [1, 盤面の一辺の長さ]
	 * @param cellData : 代入する値[1, 盤面の一辺の長さ]
	 * @throws OutOfRangeIndexException : rowIndexとcolIndexで範囲外を指定したときのエラー
	 * @throws UndefinedValueException  : cellDataで規定外の値を指定したときのエラー
	 * @throws RejectedPosionException : 問題盤面として登録されているセルを指定したときのエラー
	 */
	public void setCell(int rowIndex, int colIndex, int cellData) throws OutOfRangeIndexException, UndefinedValueException, RejectedPosionException {
		if(!(0 < rowIndex && rowIndex <= SIZE)) {
//			System.err.println("rowIndex : " + rowIndex + " : out of range!");
//			return;
			throw new Board.OutOfRangeIndexException();
		}
		if(!(0 < colIndex && colIndex <= SIZE)) {
//			System.err.println("colIndex : " + colIndex + " : out of range!");
//			return;
			throw new Board.OutOfRangeIndexException();
		}
		if(!(0 < cellData && cellData <= SIZE)) {
//			System.err.println("cellData : " + cellData + " : outside the defined range!");
//			return;
			throw new Board.UndefinedValueException();
		}
		// 問題盤面として登録されたセルは変更できないようにする
		if(defaultCells[rowIndex-1][colIndex-1] != INITNUM) {
//			System.out.println("(rowIndex, colIndex) : (" + rowIndex + ", " + colIndex + ") : you cannot put numbers in this cell!");
//			return;
			throw new Board.RejectedPosionException();
		}
		cells[rowIndex-1][colIndex-1] = cellData-1;
	}
	
	/**
	 * 盤面のセル内の値を部分的に初期化する
	 * @param rowIndex : 行のインデックス　[1, 盤面の一辺の長さ]
	 * @param colIndex : 列のインデックス [1, 盤面の一辺の長さ]
	 */
	public void resetCell(int rowIndex, int colIndex) {
		if(!(0 < rowIndex && rowIndex <= SIZE)) {
			System.err.println("rowIndex : " + rowIndex + " : out of range!");
			return;
		}
		if(!(0 < colIndex && colIndex <= SIZE)) {
			System.err.println("colIndex : " + colIndex + " : out of range!");
			return;
		}
		cells[rowIndex-1][colIndex-1] = INITNUM;
		
	}
	
	/**
	 * 数独の制約に現段階で満たしているか判定する
	 * @return boolean : 数独の条件を満たしていない部分があれば false
	 */
	public boolean isCorrect() {

		boolean correct = true;
		
		//水平方向（行）の判定
		for(int row = 0; row < cells.length; row++) {
			boolean[] check = new boolean[SIZE];
			for(int i = 0; i < SIZE; i++) {
				check[i] = false;
			}
			
			for(int i = 0; i < SIZE; i++) {
				int cell = cells[row][i];
				if(cell == INITNUM) { // 行に空欄がある場合
					continue;
				}if(check[cell]) { // 行内の数値に重複があるとき
					return false;
				}
				check[cell] = true;
			}
		}
		//垂直方向(列)の判定
		for(int col = 0; col < cells[0].length; col++) {
			boolean[] check = new boolean[SIZE];
			for(int i = 0; i < SIZE; i++) {
				check[i] = false;
			}
			
			for(int i = 0; i < SIZE; i++) {
				int cell = cells[i][col];
				if(cell == INITNUM) { // 列に空欄がある場合
					continue;
				}if(check[cell]) { // 列内の数値に重複があるとき
					return false;
				}
				check[cell] = true;
			}
		}
		//格子ごとの判定
		int sqrtSize = SIZESQRT;
		for(int row = 0; row < SIZE; row += sqrtSize) {
			for(int col = 0; col < SIZE; col += sqrtSize) {
				//格子の(0, 0)位置
				
				boolean[] check = new boolean[SIZE];
				for(int i = 0; i < SIZE; i++) {
					check[i] = false;
				}
				
				for(int i = 0; i < sqrtSize; i++) {
					for(int j = 0; j < sqrtSize; j++) {
						int cell = cells[row + i][col + j];
						if(cell == INITNUM) { // 格子に空欄がある場合
							continue;
						}if(check[cell]) { // 格子内の数値に重複があるとき
							return false;
						}
						check[cell] = true;
					}
				}

			}
		}
		
		return correct;
	}
	
	/**
	 * 数独が完成しているかを判定する
	 * @return　boolean : 数独の条件を全て満たして入れば true
	 */
	public boolean isSuccess() {
		if(!isFilled()) {
			return false;
		}
		
		boolean correct = true;
		
		//水平方向（行）の判定
		for(int row = 0; row < cells.length; row++) {
			boolean[] check = new boolean[SIZE];
			for(int i = 0; i < SIZE; i++) {
				check[i] = false;
			}
			
			for(int i = 0; i < SIZE; i++) {
				int cell = cells[row][i];
//				if(cell == INITNUM) { // 行に空欄がある場合
//					continue;
//				}if(check[cell]) { // 行内の数値に重複があるとき
//					return false;
//				}
				check[cell] = true;
			}
			
			// 行内に全ての数字が存在するかチェック
			for(boolean tf : check) {
				if(!tf) {
					correct = false;
				}
			}
		}
		//垂直方向(列)の判定
		for(int col = 0; col < cells[0].length; col++) {
			boolean[] check = new boolean[SIZE];
			for(int i = 0; i < SIZE; i++) {
				check[i] = false;
			}
			
			for(int i = 0; i < SIZE; i++) {
				int cell = cells[i][col];
//				if(cell == INITNUM) { // 列に空欄がある場合
//					continue;
//				}if(check[cell]) { // 列内の数値に重複があるとき
//					return false;
//				}
				check[cell] = true;
			}

			// 列内に全ての数字が存在するかチェック
			for(boolean tf : check) {
				if(!tf) {
					correct = false;
				}
			}
			
		}
		
		//格子ごとの判定
		int sqrtSize = SIZESQRT;
		for(int row = 0; row < SIZE; row += sqrtSize) {
			for(int col = 0; col < SIZE; col += sqrtSize) {
				//格子の(0, 0)位置
				
				boolean[] check = new boolean[SIZE];
				for(int i = 0; i < SIZE; i++) {
					check[i] = false;
				}
				
				for(int i = 0; i < sqrtSize; i++) {
					for(int j = 0; j < sqrtSize; j++) {
						int cell = cells[row + i][col + j];
//						if(cell == INITNUM) { // 格子に空欄がある場合
//							continue;
//						}if(check[cell]) { // 格子内の数値に重複があるとき
//							return false;
//						}
						check[cell] = true;
					}
				}

				// 格子内に全ての数字が存在するかチェック
				for(boolean tf : check) {
					if(!tf) {
						correct = false;
					}
				}
			}
		}

		return correct;
		
	}
	
	/**
	 * 盤面内に空欄のセルがあるか判定する
	 * @return boolean : 全て埋まっていればtrue
	 */
	public boolean isFilled() {
		for(int[] array : cells) {
			for(int cell : array) {
				if(cell == INITNUM) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 盤面のセル内が空白か判定する。
	 * @param rowIndex : 行のインデックス　[1, 盤面の一辺の長さ]
	 * @param colIndex : 列のインデックス [1, 盤面の一辺の長さ]
	 * @return boolean : 指定のセルが空白ならtrue
	 */
	public boolean isBlank(int rowIndex, int colIndex) {
		if(!(0 < rowIndex && rowIndex <= SIZE)) {
			System.err.println("rowIndex : " + rowIndex + " : out of range!");
			return false;
		}
		if(!(0 < colIndex && colIndex <= SIZE)) {
			System.err.println("colIndex : " + colIndex + " : out of range!");
			return false;
		}
		return this.cells[rowIndex-1][colIndex-1] == INITNUM;
	}
	
	/**
	 * 盤面内の残りの空欄数を返す
	 * @return　int : 空欄数
	 */
	public int countBlank() {
		int blankCells = 0;
		for(int[] array : cells) {
			for(int cell : array) {
				if(cell == INITNUM) {
					blankCells++;
				}
			}
		}
		
		return blankCells;
	}
	
	/**
	 * 盤面を整形して表示する
	 */
	public void printBoard() {
		// ANSIエスケープコードによる装飾
		//文字色一覧
        String red    = "\u001b[00;31m";
        String green  = "\u001b[00;32m";
        String yellow = "\u001b[00;33m";
        String purple = "\u001b[00;34m";
        String pink   = "\u001b[00;35m";
        String cyan   = "\u001b[00;36m";
        //特殊処理
        String bold   = "\u001b[01m";
        String uline  = "\u001b[04m";
        String end    = "\u001b[00m";
        
        
		// 表示
		System.out.println();
		System.out.print(" |");
		for(int col = 0; col < SIZE; col++) {
			// 格子の明示するための処理（タテ）
			if(col%SIZESQRT == 0 && col != 0) {
				System.out.print(" ");
			}
			System.out.print(col+1);
		}
		System.out.println(" ");
		// ヘッダーの罫線（ヨコ）
		System.out.print("-+");
		for(int col = 0; col < SIZE; col++) {
			// 格子の明示するための処理（タテ）
			if(col%SIZESQRT == 0 && col != 0) {
				System.out.print("+");
			}
			System.out.print("-");
		}
		System.out.println("+");
		for(int row = 0; row < SIZE; row++) {
			// 格子の明示するための処理（ヨコ）
			if(row%SIZESQRT == 0 && row != 0) {
				System.out.print(" +");
				for(int col = 0; col < SIZE; col++) {
					// 格子の明示するための処理（タテ）
					if(col%SIZESQRT == 0 && col != 0) {
						System.out.print("+");
					}
					System.out.print("-");
				}
				System.out.println("+");
			}
			System.out.print(row+1 + "|");
			for(int col = 0; col < SIZE; col++) {
				// 格子の明示するための処理（タテ）
				if(col%SIZESQRT == 0 && col != 0) {
					System.out.print("|");
				}
				if(cells[row][col] == INITNUM) {
					System.out.print(".");
				}else {
					if(defaultCells[row][col] != INITNUM) {
						System.out.print(green + bold + uline);
						System.out.print(cells[row][col]+1);
						System.out.print(end);
						
					}else {
						System.out.print(cells[row][col]+1);
					}
				}
			}
			System.out.println("|");
		}
		System.out.print("　+");
		for(int col = 0; col < SIZE; col++) {
			// 格子の明示するための処理（タテ）
			if(col%SIZESQRT == 0 && col != 0) {
				System.out.print("+");
			}
			System.out.print("-");
		}
		System.out.println("+");
	}
	
	/**
	 *  Boardの範囲外のセルを指定したときのエラー
	 */
	public static class OutOfRangeIndexException extends Exception {
	    /**
	     * コンストラクタ
	     */
	    OutOfRangeIndexException(){
	      super("盤面の範囲外を指定されました");
	    }
	}
	
	/**
	 *  Boardに登録できない値を指定したときのエラー
	 */
	public static class UndefinedValueException extends Exception {
		/**
		 * コンストラクタ
		 */
		UndefinedValueException(){
			super("盤面に登録できない値が指定されました");
		}
	}
	
	/**
	 * Boardに登録できない場所を指定したときのエラー
	 */
	public static class RejectedPosionException extends Exception {
		/**
		 * コンストラクタ
		 */
		RejectedPosionException(){
			super("値を登録できないセルを指定されました");
		}
	}
}
