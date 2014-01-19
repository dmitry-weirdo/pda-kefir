/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 09.11.2010 14:26:20$
*/
package su.opencode.kefir.web;

import su.opencode.kefir.srv.dynamicGrid.DynamicGridParams;
import su.opencode.kefir.srv.dynamicGrid.DynamicGridService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetGridParamServlet extends JsonServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			final DynamicGridParams params = new DynamicGridParams(getParameterMap(request));

			final DynamicGridService service = getService(DynamicGridService.class);

			final String entityName = params.getEntityName();
			final String columnName = params.getColumnName();
			switch (params.getMethodType())
			{
				case GRID_SET_COLUMN_HIDDEN:
					service.setColumnHidden(entityName, columnName, params.getHidden());
					break;

				case GRID_SET_COLUMN_WIDTH:
					service.setColumnWidth(entityName, columnName, params.getNewWidth());
					break;

				case GRID_CHANGE_COLUMNS_ORDER:
					service.changeColumnsOrder(entityName, columnName, params.getColumnIndex(), params.getNewIndex());
					break;

				case GRID_SORT:
					service.setSort(entityName, columnName, params.getSortOrder(), params.getSortDirection(), params.getClear());
					break;

				case GRID_DEFAULT_SORT:
					service.setDefaultSort(entityName, params.getColumns(), params.getDirections());
					break;

				default:
					throw new RuntimeException("Incorrect method type for DynamicGrid");
			}

			writeSuccess(response);
		}
		catch (Exception e)
		{
			writeFailure(response, e);
		}
	}

	private static final int GRID_SET_COLUMN_HIDDEN = 0;
	private static final int GRID_SET_COLUMN_WIDTH = 1;
	private static final int GRID_CHANGE_COLUMNS_ORDER = 2;
	private static final int GRID_SORT = 3;
	private static final int GRID_DEFAULT_SORT = 4;
}