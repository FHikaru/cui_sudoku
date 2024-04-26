package main;

import java.util.Scanner;

/**
 * 数独を遊べるゲーム管理クラス
 */
public class Sudoku {
	
	private Scanner scan;
	
	private Board board;
	private final int BOARDSIZE = 4;
	
	
	/**
	 * コンストラクタ
	 * 利用するボードをメンバとして登録する
	 */
	public Sudoku() {
		scan = new Scanner(System.in);
		board = new Board(BOARDSIZE);
		System.out.println("解の個数 : " + countBoardAnswer(board));
	}
	
//	public Sudoku(Scanner scan) {
//		this.scan = scan;
//		board = new Board();
//	}
	
//	/**
//	 * 答えが一意に決まる盤面を作成する。
//	 */
//	private void genUniqueBoard() {
//		for(int row = 1; row <= BOARDSIZE; row++) {
//			for(int col = 1; col <= BOARDSIZE; col++) {
//				for(int num = 1; num <= BOARDSIZE; num++) {
//					board.setCell(row, col, num);
//				}
//			}
//		}
//	}
	
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
		
		for(int row = 1; row <= BOARDSIZE; row++) {
			for(int col = 1; col <= BOARDSIZE; col++) {
				for(int num = 1; num <= BOARDSIZE; num++) {
					int r, c, n;
					r = ((rStart + row) % BOARDSIZE )+1;
					c = ((cStart + col) % BOARDSIZE )+1;
					n = ((nStart + num) % BOARDSIZE )+1;
					if(board.isBlank(r, c)) {
						Board next = board.copyBoard();
						next.setCell(r, c, n);
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
			//入力をboardに反映
			board.setCell(
					inputRestriction(1, board.SIZE+1, "行"),
					inputRestriction(1, board.SIZE+1, "列"),
					inputRestriction(1, board.SIZE+1, "値")
					);
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
//			System.out.print(String.format("%s[%d, %d) : ", description, lowerLimit, upperLimit));
			System.out.print(description + "[" + lowerLimit + ", " + (upperLimit-1) + "] : ");
			num = scan.nextInt();
			
			if(lowerLimit <= num && num < upperLimit) {
				break;
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
	
	

}
