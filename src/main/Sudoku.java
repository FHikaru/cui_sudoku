package main;

import java.util.Scanner;

/**
 * 数独を遊べるゲーム管理クラス
 */
public class Sudoku {
	private final int DEFAULTSIZE = 9;
	private final double BLANKRATE = 0.60;
	
	private Scanner scan;
	
	private Board board;
	private int size = DEFAULTSIZE;
	
	
	/**
	 * コンストラクタ
	 * 利用するボードをメンバとして登録する
	 */
	public Sudoku() {
		this.size = DEFAULTSIZE;
		scan = new Scanner(System.in);
		board = new Board(size);
//		board = genUniqueBoard(board);
//		board = genAnswerBoard(BOARDSIZE);
		board = genUniqueBoardAlt();
		board.registerCells();
//		System.out.println("解の個数 : " + countBoardAnswer(board));
	}
	
	/**
	 * コンストラクタ
	 * 利用するボードをメンバとして登録する
	 * @param size : int : 盤面のサイズ（平方数である必要がある）
	 */
	public Sudoku(int size) {
		//sizeが平方数かの判定
		int sizeSqrt = (int)Math.sqrt((double)size);
		if(size != sizeSqrt * sizeSqrt) {
			System.err.println("引数が平方数ではありません");
		}

		this.size = size;
		scan = new Scanner(System.in);
		board = new Board(size);
//		board = genUniqueBoard(board);
//		board = genAnswerBoard(BOARDSIZE);
		board = genUniqueBoardAlt();
		board.registerCells();
//		System.out.println("解の個数 : " + countBoardAnswer(board));
	}
	
	public Sudoku(Scanner scan, int size) {
		//sizeが平方数かの判定
		int sizeSqrt = (int)Math.sqrt((double)size);
		if(size != sizeSqrt * sizeSqrt) {
			System.err.println("引数が平方数ではありません");
		}

		this.size = size;
		this.scan = scan;
		board = new Board(size);
		board = genUniqueBoardAlt();
		board.registerCells();
	}
	
	/**
	 * 白紙から回答を作成する。
	 * （作成される回答集合は解の全体集合以下である）
	 * @param size : int : 盤面のサイズ(平方数である必要がある)
	 * @return　Board : 数独が完成している盤面
	 */
	private Board genAnswerBoard(int size) {
		//sizeが平方数かの判定
		int sizeSqrt = (int)Math.sqrt((double)size);
		if(size != sizeSqrt * sizeSqrt) {
			System.err.println("引数が平方数ではありません");
			return null;
		}
		
		Board board = new Board(size);
		
		int[] numLine = new int[size];
		int[] startOffset = new int[sizeSqrt];
		int[][] sectionOffset = new int[sizeSqrt][sizeSqrt];
		for(int i = 0; i < sizeSqrt; i++) {
			startOffset[i] = i;
			for(int j = 0; j < sizeSqrt; j++) {
				sectionOffset[i][j] = j;
				numLine[(i*sizeSqrt)+j] = (i*sizeSqrt)+j + 1;
			}
		}
		
		//数字列と基準点をランダムにする
		Sudoku.shuffle(numLine);
		Sudoku.shuffle(startOffset);
		for(int i = 0; i < sizeSqrt; i++) {
			Sudoku.shuffle(sectionOffset[i]);
		}
		
		for(int row = 0; row < size; row++) {
			
			for(int colGridOut = 0; colGridOut < sizeSqrt; colGridOut++) {
				for(int colGridIn = 0; colGridIn < sizeSqrt; colGridIn++) {

					int r = (row) % 9;
					int c = (((colGridOut) * sizeSqrt ) + (colGridIn)) % size;
					int n = numLine[(((colGridOut + sectionOffset[(int)row/sizeSqrt][row%sizeSqrt]) * sizeSqrt )
							+ (colGridIn + startOffset[(int)row/sizeSqrt])) % size];
					try {
						board.setCell(r+1, c+1, n);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return board;
	}
	
	/**
	 * 解がユニークな盤面を作成して返す関数。
	 * 回答となる盤面をgenAnswerBoardで作成してそこから解がユニークを維持してヒントを減らして生成する。
	 * @return Board : 作成された盤面
	 */
	private Board genUniqueBoardAlt() {
		Board board = genAnswerBoard(size);
		
		
		while(true) {
			int row = (int)(Math.random()*size) + 1;
			int col = (int)(Math.random()*size) + 1;
			Board next = board.copyBoard();
			next.resetCell(row, col);
			
			if(countBoardAnswer(next) != 1 ) {
//				System.out.println(size*size*BLANKRATE);
				if(board.countBlanks() < size*size*BLANKRATE){
					board = genAnswerBoard(size);
					continue;
				}
				break;
			}
			board = next;
		}
		
		return board;
	}
	
	
	/**
	 * 解がユニークな盤面を作成して返す関数
	 * 引数の盤面から全探索して解を作成する。
	 * @param board : Board : 種となる盤面
	 * @return Board : 作成された盤面(解がない場合はnull)
	 */
	private Board genUniqueBoard(Board board) {
		//現盤面がすでに数独の制約を満たさないなら枝刈り(再帰探索の打ち切り)をする。
		if(!board.isCorrect()) {
			return null;
		}
//		board.printBoard();
		
		boolean selected = false;

		int rStart, cStart, nStart;

		rStart = (int)(Math.random()*size);
		cStart = (int)(Math.random()*size);
		nStart = (int)(Math.random()*size);

		for(int row = 1; row <= size; row++) {
			for(int col = 1; col <= size; col++) {
				for(int num = 1; num <= size; num++) {
					int r, c, n;
					r = ((rStart + row) % size )+1;
					c = ((cStart + col) % size )+1;
					n = ((nStart + num) % size )+1;
					if(board.isBlank(r, c)) {
						Board next = board.copyBoard();
						try {
							next.setCell(r, c, n);
						}catch(Exception e) {
							e.printStackTrace();
						}
						Board ans = genUniqueBoard(next);
						if(ans == null) {
							continue;
						}
						
						if(1 == countBoardAnswer(board)) {
							return board;
						}else {
							return ans;
						}
					}
				}
			}
		}
		if(board.isSuccess()) {
			return board;
		}
		return null;
	}
	
	/**
	 * 引数の盤面が持つ解の個数を返す
	 * @param board : 解の個数が知りたい盤面
	 * @return int : 解の個数
	 */
	private int countBoardAnswer(Board board) {
		//現盤面がすでに数独の制約を満たさないなら枝刈り(再帰探索の打ち切り)をする。
		if(!board.isCorrect()) {
			return 0;
		}
		
		boolean selected = false;
		
		int counter = 0;
		int rStart, cStart, nStart;

//		rStart = (int)(Math.random()*BOARDSIZE);
//		cStart = (int)(Math.random()*BOARDSIZE);
//		nStart = (int)(Math.random()*BOARDSIZE);
		rStart = 0;
		cStart = 0;
		nStart = 0;
		
		for(int row = 1; row <= size; row++) {
			for(int col = 1; col <= size; col++) {
				for(int num = 1; num <= size; num++) {
					int r, c, n;
					r = ((rStart + row) % size )+1;
					c = ((cStart + col) % size )+1;
					n = ((nStart + num) % size )+1;
					if(board.isBlank(r, c)) {
						Board next = board.copyBoard();
						try {
							next.setCell(r, c, n);
						}catch(Exception e) {
							e.printStackTrace();
						}
//						next.printBoard();
						counter += countBoardAnswer(next);
						selected = true;
					}
				}
				if(selected) {
					if(board.isSuccess()) {
						counter++;
//						board.printBoard();
					}
					return counter;
				}
			}
		}
		if(board.isSuccess()) {
			counter++;
//			board.printBoard();
		}
		return counter;
	}
	
	/**
	 * ゲームを開始して終了まで続ける
	 */
	public void execGame() {
		board.printBoard();
		
		while(!board.isFilled()) {
			try {
				//入力をboardに反映
				board.setCell(
						inputRestriction(1, board.SIZE+1, "行"),
						inputRestriction(1, board.SIZE+1, "列"),
						inputRestriction(1, board.SIZE+1, "値")
						);
			}catch(Board.OutOfRangeIndexException e) {
				System.out.println("盤面の範囲外が指定されました");
				System.out.println("もう一度入力してください");
				
			}catch(Board.UndefinedValueException e) {
				System.out.println("入力できない値が指定されました");
				System.out.println("もう一度入力してください");
				
			}catch(Board.RejectedPosionException e) {
				System.out.println("指定した場所は初期値のため変更することができません");
				System.out.println("もう一度入力してください");
				
			}
			//表示
			board.printBoard();
		}
		
		judgeResult();
		

	}
	
	/**
	 * 指定の入力のみを受け付ける標準入力関数
	 * @param lowerLimit : int : 入力できる下限値
	 * @param upperLimit : int : 入力できる上限値（upperLimitは入力できる値に含めない）
	 * @param description : String : 入力前に表示する受け取りたい値名
	 * @return　int : 標準入力された値[lowerLimit, upperLimit)
	 */
	private int inputRestriction(int lowerLimit, int upperLimit, String description) {
//		Scanner scan = new Scanner(System.in);
		int num = 0;
		while(true) {
			try {
//				System.out.print(String.format("%s[%d, %d) : ", description, lowerLimit, upperLimit));
				System.out.print(description + "[" + lowerLimit + ", " + (upperLimit-1) + "] : ");

				num = scan.nextInt();
				
				if(lowerLimit <= num && num < upperLimit) {
					break;
				}else {
					System.out.println("[" + lowerLimit + ", " + (upperLimit-1) + "]の範囲外の数値が入力されました");
				}
			}catch(Exception e) {
				scan.nextLine();
				System.out.println("数値以外が入力されました");
			}

			System.out.println("もう一度入力してください。");
		}
		return num;
	}

	
	/**
	 * 盤面状況をから勝敗判定、表示を行う。
	 */
	private void judgeResult() {
		//正解か判定
		System.out.println(board.isSuccess() ? "Complete!" : "Faild...");
		
	}
	
	/**
	 * Fisher–Yates shuffleを用いた配列(array)の入れ替えアルゴリズム
	 * 引数に対して、破壊的処理を行う。
	 * @param array : int[] : 入れ替え対象の配列
	 */
	public static void shuffle(int[] array) {
	    // 配列が要素１つか空ならそのまま終了
	    if (array.length <= 1) {
	        return;
	    }

	    // Fisher–Yates shuffle
	    for (int i = array.length - 1; i > 0; i--) {
	        int idx = (int)(Math.random() * (i+1));
	        // 要素入れ替え(swap)
	        int tmp = array[idx];
	        array[idx] = array[i];
	        array[i] = tmp;
	    }
	}

}
