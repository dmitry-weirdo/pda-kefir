/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 20.03.2012 18:55:56$
*/
package su.opencode.kefir.gen.project;

public class ProjectConfig
{
	/**
	 * ������� ����������, ������ ������� ��������� ������.
	 */
	public String baseDir;

	/**
	 * �������� �������.
	 */
	public String projectName;

	/**
	 * �������� ���������� ������ �������.
	 * �� ��������� � (�������� ������� + "Srv")
	 */
	public String srvModuleName;

	/**
	 * �������� ���-������ �������.
	 * �� ��������� � (�������� ������� + "Web")
	 */
	public String webModuleName;

	/**
	 * �������� Selenium-������ �������.
	 * �� ��������� � (�������� ������� + "Selenium")
	 */
	public String seleniumModuleName;

	/**
	 * ���� � ����������, ���������� ����� ���������� ��� selenium-����� ����������, ������������ ������� ���������� selenium-�����.
	 * �� ��������� � "lib"
	 */
	public String seleniumLibPath;

	/**
	 * ����� ��� ������
	 */
	public String seleniumBasePackage;

	/**
	 * ������ �������� �������� (���������) ������ ��� ���������� ������.
	 */
	public String srvBasePackage;

	/**
	 * ������ �������� ������, � ������� ����� ���������� ����� Renderers � ���� renderers.properties � �������� Excel-���������� ����������.
	 * �� ��������� � ({@linkplain #srvBasePackage ������� ����� ���������� ������} + ".render")
	 */
	public String renderersPackage;

	/**
	 * ������ �������� �������� (���������) ������ ��� ���-������.
	 */
	public String webBasePackage;

	/**
	 * �������� persistenceUnit (����������� � persistence.xml).
	 * �� ��������� ����� �������� �������.
	 */
	public String persistenceUnitName;

	/**
	 * �������� jtaDataSource ��� �������� "java' (����������� � persistence.xml).
	 * �� ��������� � (�������� ������� + "DS").
	 */
	public String jtaDataSourceName;

	/**
	 * ContextPath ����������. ����������� � �������� &lt;context-root&gt; � jboss-web.xml.
	 * �� ��������� - ����� �������� �������
	 */
	public String contextPath;

	/**
	 * ContextPath, �� �������� ��������� extJS. ������������ ��� ��������� js-������ � kefirInclude.jspf.
	 * �� ��������� - "/ext"
	 */
	public String extContextPath;

	/**
	 * ContextPath, �� �������� ��������� kefirStatic.war. ������������ ��� ��������� js-������ � kefirInclude.jspf.
	 * �� ��������� - "/kefirStatic"
	 */
	public String kefirStaticContextPath;

	/**
	 * ��������� (title) html-�������� index.jsp.
	 */
	public String htmlTitle;

	/**
	 * ��������� �������� ����.
	 * �� ��������� - ����� {@linkplain #htmlTitle ��������� html-��������}.
	 */
	public String mainMenuTitle;

	/**
	 * ������ ������ �������� ����.
	 * �� ��������� � {@linkplain ProjectConfigUtils#DEFAULT_MAIN_MENU_BUTTON_WIDTH 400 ���������}.
	 */
	public String mainMenuButtonWidth;

	/**
	 * Js-���������, � ������� ����� ������ js-����� "MainMenu" ���������� � ������� ����� ����������.
	 * �� ��������� � ����� �������� �������.
	 */
	public String mainMenuNamespace;

	/**
	 * �����, � ������� ����� ����������� ����� ��� ������.
	 * �� ��������� � {@linkplain #srvBasePackage ����� �������� ������ ��� ���������� ������}.
	 */
	public String addressClassPackage;

	/**
	 * Js-namespace ��� ����� ������.
	 * �� ��������� � �����, � ������� ��������� ����� �������� + "address".
	 */
	public String addressJsNamespace;

	/**
	 * Js form namespace ��� ����� ������.
	 * �� ��������� "Address".
	 */
	public String addressFormJsNamespace;

	/**
	 * ���� � ����������, ���������� ����� ���������� ��� ��������� � ��� ������ ����������, ������������ ������� ���������� �������.
	 * �� ��������� � "lib"
	 */
	public String commonLibPath;

	/**
	 * ���� � ����������, ���������� ���������� ��� ��������� ����� ����������, ������������ ������� ���������� ��������� �����.
	 * �� ��������� � "lib"
	 */
	public String srvLibPath;

	/**
	 * ���� � ����������, ���������� ����� ���������� ��� ���-����� ����������, ������������ ������� ���������� ���-�����.
	 * �� ��������� � "lib"
	 */
	public String webLibPath;

	/**
	 * ���� � ����������, �� ������� ����� ������� ����� ��� ��������� � lib ���������� ���������� �������.
	 * ���������� ������ ��������� ������������� "common", "srv", "web" � "selenium".
	 */
	public String libPath;

	/**
	 * ���� � ����������, �� ������� ����� ������� ��������-����� ��� ��������� �������.
	 * ���������� ������ ��������� ��������-���� addressTemplate.js.
	 */
	public String templatePath;

	/**
	 * �������� jar-�����, ����������� ��� ��������� �����.
	 * �� ��������� � (�������� ���������� ������ + ".jar")
	 */
	public String srvJarName;

	/**
	 * �������� war-�����, ����������� ��� ��� �����.
	 * �� ��������� � (�������� ���-������ + ".war")
	 */
	public String webWarName;

	/**
	 * �������� ear-�����, ����������� ��� ���������� � ���������� �� ������.
	 * �� ��������� � (�������� ������� + ".ear")
	 */
	public String earName;

	/**
	 * ���� ��� ������ ����������.
	 * ����� ��������� � build.properties ��� ���������������� �������.
	 */
	public String deployPath;

	/**
	 * ���� ��� ������ ����� ��������� ���������� (kefirUtil, json ���)
	 * ����� ��������� � build.properties ��� ���������������� �������.
	 */
	public String deployLib;
}