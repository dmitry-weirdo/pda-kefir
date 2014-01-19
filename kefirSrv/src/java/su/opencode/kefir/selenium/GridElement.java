/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.kefir.selenium;

import static su.opencode.kefir.util.StringUtils.concat;

public class GridElement extends AbstractPage
{
	public GridElement(String gridId) {
		super(gridId);
		this.gridId = gridId;

		final String gridXpath = concat(sb, "//div[@id='", gridId, "']");
		gridBodyXpath = concat(sb, gridXpath, "/div/div[contains(@class, 'x-panel-body')]/div/div[2]/div[2]/div[@class='x-grid3-body']/div");
		gridFirstRowXpath = concat(sb, gridBodyXpath, "[contains(@class, 'x-grid3-row-first')]");
		gridFirstCellFirstRowXpath = concat(sb, gridFirstRowXpath, "/table/tbody/tr/td[contains(@class, 'x-grid3-cell-first')]");
		gridLoadMaskXpath = concat(sb, "div[@id='", gridId, "']/div/div[contains(@class, 'ext-el-mask-msg')]");
	}

	public GridElement clickOnFirstRow() {
		clickByXpath(gridFirstRowXpath);

		return this;
	}

	public GridElement clickOnRow(int rowIndex) {
		clickByXpath(concat(sb, gridBodyXpath, "[", rowIndex, "]"));

		return this;
	}

	public GridElement clickOnFirstCellFirstRow() {
		clickByXpath(gridFirstCellFirstRowXpath);

		return this;
	}

	public int rowCount() {
		return getElementsCountByXpath(gridBodyXpath);
	}


	public GridElement fillSearchField(String fieldId, Object value) {
		setValueAndPressEnterById(fieldId, value);
		waitUntilElementEnabledXpath(gridLoadMaskXpath);

		return this;
	}

	private String gridId;
	private String gridBodyXpath;
	private String gridFirstRowXpath;
	private String gridFirstCellFirstRowXpath;
	private String gridLoadMaskXpath;
}
