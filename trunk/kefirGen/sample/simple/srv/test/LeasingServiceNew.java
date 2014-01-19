/**
 Copyright 2012 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package su.opencode.minstroy.ejb.leasing;

import su.opencode.kefir.srv.SortConfig;

import javax.ejb.Local;
import java.util.List;

import su.opencode.kefir.srv.VO;
//	${APPEND_IMPORT}

@Local
public interface LeasingServiceNew
{
	List<ParcelVO> getParcels(ParcelFilterConfigNew filterConfig, SortConfig sortConfig);
	int getParcelsCount(ParcelFilterConfigNew filterConfig);

	Parcel getParcel(Integer id);
	<T extends VO> T getParcelVO(Integer id, Class<T> voClass);
	Integer createParcel(Parcel parcel);
	void updateParcel(Integer id, Parcel parcel);
	void deleteParcel(Integer id);


	// ${APPEND_METHOD_DECLARATION}
}