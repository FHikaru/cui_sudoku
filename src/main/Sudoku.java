package main;

import java.util.Scanner;

/**
 * 数独を遊べるゲーム管理クラス
 */
public class Sudoku {
	private Scanner scan;
	
	private Board board;
	
	
	
	/**
	 * コンストラクタ
	 * 利用するボードをメンバとして登録する
	 */
	public Sudoku() {
		scan = new Scanner(System.in);
		board = new Board();
	}
	
//	public Sudoku(Scanner scan) {
//		this.scan = scan;
//		board = new Board();
//	}
	
	/**
	 * ゲームを開始して終了まで続ける
	 */
	public void execGame() {
		board.printBoard();
		
		while(!board.isFilled()) {
			//入力をboardに反映
			board.setCell(
					inputRestriction(1, Board.SIZE+1, "行"),
					inputRestriction(1, Board.SIZE+1, "列"),
					inputRestriction(1, Board.SIZE+1, "値")
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
