/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ rybakin
 $Revision$
 $Date: 24.05.2011 16:27:05$
*/
package su.opencode.kefir.srv;

public class ClientException extends RuntimeException//todo: ���������� ��� �������������� RuntimeExceptions �� ���
{
	protected ClientException() {
	}
	public ClientException(String message) {
		super(message);
	}
}
