package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import base.TestBase;

public class TestUtil extends TestBase {

	/*******************************************
	 * Screen Capture Method
	 *******************************************/
	public static String screenshotName;

	public static void captureScreenshot() throws IOException {
		/**
		 * This screenshot will store it to memory, not in any file To store the
		 * screenshot we need an utility from Apache - FileUtils.copyFile This
		 * utility capable of throwing exception
		 */
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date currentDate = new Date();
		screenshotName = currentDate.toString().replace(":", "_").replace(" ", "_") + ".jpg";
		FileUtils.copyFile(srcFile,
				new File(System.getProperty("user.dir") + "\\test-output\\html\\" + screenshotName));

	}

	/*******************************************
	 * DataProvider Method
	 *******************************************/
	public static Object[][] getDataFromExcel(String sheetName) {
		// Get rows count
		int rows = excel.getRowCount(sheetName);

		// Get columns count
		int cols = excel.getColumnCount(sheetName);

		String[][] dataFromExcel = new String[rows - 1][cols];

		for (int rowNum = 1; rowNum < rows; rowNum++) {
			int startFromFirstColumn = rowNum + 1;
			for (int colNum = 0; colNum < cols; colNum++) {
				dataFromExcel[rowNum - 1][colNum] = excel.getCellData(sheetName, colNum, startFromFirstColumn);
			}
		}

		return dataFromExcel;
	}
}
