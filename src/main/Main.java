package main;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// 数独を作る！
		Board board = new Board();

		//表示
		board.printBoard();
		
		//入力（１回だけ）
		Scanner scan = new Scanner(System.in);
		int rowIndex = 0;
		int colIndex = 0;
		int cellData = 0;
		
		while(!board.isFilled()) {
			System.out.print("行："); rowIndex = scan.nextInt();
			System.out.print("列："); colIndex = scan.nextInt();
			System.out.print("値："); cellData = scan.nextInt();
			//入力をboardに反映
			board.setCell(rowIndex-1, colIndex-1, cellData);
			//表示
			board.printBoard();
		}
		
		
		//正解か判定
		System.out.println(board.isSuccess() ? "You Win!" : "Faild...");
		
	}

}
