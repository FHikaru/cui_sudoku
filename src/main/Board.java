package main;

/**
 *　数独の盤面
 */
public class Board {
	public final int SIZE; //盤面の一辺の長さ[平方数]
	private static final int INITNUM = -1; //盤面の初期値
	
	private int[][] cells; //盤面そのもの。値は[0, SIZE)
	
	/**
	 * コンストラクタ
	 */
	public Board(int size) {
		SIZE = size;
		cells = new int[SIZE][SIZE];
		//初期化
		for(int row = 0; row < SIZE; row++) {
			for(int column = 0; column < SIZE; column++) {
				cells[row][column] = INITNUM;
			}
		}
	}
	
	/**
	 * コピーコンストラクタ
	 * Boardインスタンスで初期化する
	 * @param board　: Board : コピー元のBoard
	 */
	public Board(Board board) {
		this.SIZE = board.SIZE;
		this.cells = new int[board.SIZE][board.SIZE];
		for(int row = 0; row < board.SIZE; row++) {
			for(int column = 0; column < board.SIZE; column++) {
				cells[row][column] = board.cells[row][column];
			}
		}
	}
	
	/**
	 * コピー生成
	 * @return　Board : コピー後のBoard
	 */
	public Board copyBoard() {
		return new Board(this);
	}
	
	/**
	 * 盤面のセル内に値を代入する
	 * @param rowIndex : 行のインデックス　[1, 盤面の一辺の長さ]
	 * @param colIndex : 列のインデックス [1, 盤面の一辺の長さ]
	 * @param cellData : 代入する値[1, 盤面の一辺の長さ]
	 */
	public void setCell(int rowIndex, int colIndex, int cellData) {
		if(!(0 < rowIndex && rowIndex <= SIZE)) {
			System.err.println("rowIndex : " + rowIndex + " : out of range!");
			return;
		}
		if(!(0 < colIndex && colIndex <= SIZE)) {
			System.err.println("colIndex : " + colIndex + " : out of range!");
			return;
		}
		if(!(0 < cellData && cellData <= SIZE)) {
			System.err.println("cellData : " + cellData + " : outside the defined range!");
			return;
		}
		cells[rowIndex-1][colIndex-1] = cellData-1;
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
		int sqrtSize = (int)Math.sqrt(SIZE);
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
		int sqrtSize = (int)Math.sqrt(SIZE);
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
		// 表示
		System.out.println();
		System.out.println(" |1234");
		System.out.println("-+----");
		for(int row = 0; row < SIZE; row++) {
			System.out.print(row+1 + "|");
			for(int column = 0; column < SIZE; column++) {
				if(cells[row][column] == INITNUM) {
					System.out.print(".");
				}else {
					System.out.print(cells[row][column]+1);
				}
			}
			System.out.println();
		}
	}
}
