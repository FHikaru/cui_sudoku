package main;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		String input;
		boolean isDigit = true;
		
		Sudoku game;


		System.out.println("======= 数独 =======");
		System.out.println("-- 盤面のサイズ指定 -- ");
		System.out.println("・標準サイズ: 9");
		System.out.println("・ミニサイズ: 4");
		System.out.println("*それ以外の場合は標準サイズで開始します");
		System.out.print(": ");
		input = scan.next();
		//入力が数値であるか判定
		for (int i = 0; i < input.length(); i++) {
			isDigit = Character.isDigit(input.charAt(i));
			if (!isDigit) {
				break;
			}
		}
		if(isDigit) {
			int size = Integer.parseInt(input);
			if(size == 4) {
				game = new Sudoku(scan, 4);
			}else {
				game = new Sudoku(scan, 9);
			}
		}else {
			game = new Sudoku(scan, 9);
		}

		// 数独を起動する
		game.execGame();

	}

}
