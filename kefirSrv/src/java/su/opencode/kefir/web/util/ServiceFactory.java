/*
 Copyright 2010 LLC "Open Code"
 http://www.o-code.ru
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 14.09.2010 13:21:17$
*/
package su.opencode.kefir.web.util;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Hashtable;

import static javax.naming.Context.*;
import static su.opencode.kefir.util.StringUtils.concat;
import static su.opencode.kefir.util.StringUtils.notEmpty;

/**
 * Класс для доступа к stateless бинам на сервере jboss.
 * Режим поиска сервисов задается параметром контекста <b>ru.kg.taglib.ServiceFactory.MODE</b>.
 * Режим поиска принимает следующие значения:
 * <ul><li>
 * remote - поиск производится на удаленном сервере.
 * Сервис ищется по имени ServiceBean/remote.
 * Ссылка на сервис считается remote интерфейсом.
 * </li><li>
 * local - поиск производится на локальном сервере.
 * Сервис ищется по имени ServiceBean/local.
 * Ссылка на сервис считается local интерфейсом.
 * </li><li>
 * local-ref - поиск производится в локальном контексте.
 * Сервис ищется по имени java:comp/env/ejb/Service.
 * Ссылка на сервис считается local интерфейсом.
 * </li></ul>
 * Параметры инициализации InitialContext ищутся в
 * переменных окружения, затем в ServletContext.
 * Если параметры инициализации не найдены, то
 * используется InitialContext по-умолчанию.
 * Сброс кэша сервисов осуществляется через {@link su.opencode.kefir.web.util.CharsetFilter}
 *
 * @author lyamshin (25.01.2007 15:58:17)
 * @version $Revision$
 */
public class ServiceFactory
{
	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Аналогичен вызову get(serviceClass, true).
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @return ссылка на экземпляр сервиса.
	 */
	public static <T> T get(Class<T> serviceClass) {
		return get(serviceClass, true);
	}

	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Аналогичен вызову get(serviceClass, true).
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @param jndiBindName имя которое присвоено сервису
	 * @return ссылка на экземпляр сервиса.
	 */
	public static <T> T get(Class<T> serviceClass, String jndiBindName) {
		return get(serviceClass, true, jndiBindName);
	}

	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Используется ссылка на фабрику из thread-local переменной.
	 * Если переменная не инициализирована, то возбуждается исключение.
	 * Ссылка на фабрику инициализируется в {@link CharsetFilter}.
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @param useCache		 true если экземпляр сервиса нужно кэшировать.
	 * @return ссылка на экземпляр сервиса.
	 */
	public static <T> T get(Class<T> serviceClass, boolean useCache) {
		ServiceFactory factory = instance.get();
		if (factory == null)
			throw new IllegalStateException("Service factory not initialized");

		if (!useCache)
			return factory.getService(serviceClass);

		String key = serviceClass.getName();

		//noinspection unchecked
		T obj = (T) factory.cache.get(key);
		if (obj == null)
			factory.cache.put(key, obj = factory.getService(serviceClass));

		return obj;
	}

	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Используется ссылка на фабрику из thread-local переменной.
	 * Если переменная не инициализирована, то возбуждается исключение.
	 * Ссылка на фабрику инициализируется в {@link CharsetFilter}.
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @param useCache		 true если экземпляр сервиса нужно кэшировать.
	 * @param jndiBindName имя которое присвоено сервису
	 * @return ссылка на экземпляр сервиса.
	 */
	public static <T> T get(Class<T> serviceClass, boolean useCache, String jndiBindName) {
		ServiceFactory factory = instance.get();
		if (factory == null)
			throw new IllegalStateException("Service factory not initialized");

		if (!useCache)
			return factory.getService(serviceClass, jndiBindName);

		String key = serviceClass.getName();

		//noinspection unchecked
		T obj = (T) factory.cache.get(key);
		if (obj == null)
			factory.cache.put(key, obj = factory.getService(serviceClass, jndiBindName));

		return obj;
	}

	/**
	 * Конфигурирует фабрику.
	 * Конфигурация сохраняется в сервлет контексте в
	 * атрибуте с именем {@link ServiceFactory#CONTEXT_KEY}.
	 * Копия конфигурации возвращается в качестве возвращаемого значения.
	 *
	 * @param servletContext контекст контейнера.
	 * @return конфигурация фабрики. null - фабрика не сконфигурирована.
	 */
	static Config configure(ServletContext servletContext) {
		String mode = servletContext != null ? servletContext.getInitParameter(CONFIG_KEY) : "remote";
		if (mode == null)
			return null;

		String earName = servletContext == null ? null : servletContext.getInitParameter(EAR_NAME_KEY);

		Config config;
		if (LOCAL_REF_MODE.equalsIgnoreCase(mode))
			config = new Config(false, null, "java:comp/env/ejb", null, earName);
		else if (LOCAL_MODE.equalsIgnoreCase(mode))
			config = new Config(false, "Bean/local", null, getEnvironment(servletContext), earName);
		else if (REMOTE_MODE.equalsIgnoreCase(mode))
			config = new Config(true, "Bean/remote", null, getEnvironment(servletContext), earName);
		else
			throw new IllegalArgumentException("Invalid factory operation mode");

		StringBuilder sb = new StringBuilder();
		logger.info( concat(sb, ServiceFactory.class.getSimpleName(), " configured:") );
		logger.info( concat(sb, "\treference mode: ", mode) );
		logger.info( concat(sb, "\tearName: ", earName));
		logger.info( concat(sb, "\tconfig.context: ", config.context));
		logger.info( concat(sb, "\tconfig.posfix: ", config.postfix));

		if (servletContext != null)
			servletContext.setAttribute(CONTEXT_KEY, config);
		return config;
	}

	/**
	 * Регистрирует экземпляр фабрики в threal local переменной.
	 *
	 * @param factoryConfig конфигурация фабрики.
	 */
	static void register(Config factoryConfig) {
		instance.set(new ServiceFactory(factoryConfig));
	}

	/**
	 * Убирает экземпляр фабрики из threal local переменной.
	 */
	static void unregister() {
		instance.remove();
	}

	/**
	 * Cоздает экземпляр фабрики по готовой конфигурации.
	 *
	 * @param config конфигурация фабрики.
	 */
	private ServiceFactory(Config config) {
		this.config = config;
		// Naming Context is lazily initialized in method getContext()
		/*try
		{
			Context context = new InitialContext(config.environment);
			if (config.context != null)
				context = (Context) context.lookup(config.context);


			this.context = context;
		}
		catch (NamingException e)
		{
			throw new RuntimeException(e);
		}*/
	}

	/**
	 * Создает новый экземпляр фабрики.
	 * Читает конфигурацию из атрибута сервлет контекста. Если конфигурация не найдена, то она считывается через
	 * {@link ServiceFactory#configure(javax.servlet.ServletContext)}.
	 *
	 * @param servletContext контекст контейнера.
	 */
	public ServiceFactory(ServletContext servletContext) {
		Config config = servletContext != null ? (Config) servletContext.getAttribute(CONTEXT_KEY) : null;
		if (config == null)
		{
//            throw new IllegalStateException("ServiceFactory not configured");
			config = ServiceFactory.configure(servletContext);
		}
		this.config = config;
		// Naming Context lazily initialized in metod getContext()
		/*try
		{
			Context context = new InitialContext(config.environment);
			if (config.context != null)
				context = (Context) context.lookup(config.context);


			this.context = context;
		}
		catch (NamingException e)
		{
			throw new RuntimeException(e);
		}*/
	}

	public ServiceFactory() {
		this((ServletContext) null);
	}

	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Кэширование не используется.
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @return ссылка на экземпляр сервиса.
	 */
	public <T> T getService(Class<T> serviceClass) {
		StringBuilder sb = new StringBuilder();

		if ( notEmpty(config.earName) )
			sb.append(config.earName).append("/");

		sb.append(serviceClass.getSimpleName());

		if ( notEmpty(config.postfix) )
			sb.append(config.postfix);

		String name = sb.toString();

		logger.info( concat("Service jndi lookup name: ", name) );
		return getService(serviceClass, name);
	}

	/**
	 * Возвращает ссылку на экземпляр сервиса.
	 * Кэширование не используется.
	 *
	 * @param serviceClass класс бизнес интерфейса сервиса.
	 * @param jndiBindName имя которое присвоено сервису
	 * @return ссылка на экземпляр сервиса.
	 */
	public <T> T getService(Class<T> serviceClass, String jndiBindName) {
		try
		{
			Object instance = getContext().lookup(jndiBindName);
			if (config.remote)
				instance = javax.rmi.PortableRemoteObject.narrow(instance, serviceClass);

			//noinspection unchecked
			return (T) instance;
		}
		catch (NamingException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Initializate naming context lazily
	 *
	 * @return configured context
	 */
	private Context getContext() {
		if (this.context == null)
		{
			try
			{
				Context context = config.remote ? new InitialContext(config.environment) : new InitialContext();
				if (config.context != null)
					context = (Context) context.lookup(config.context);

				this.context = context;
			}
			catch (NamingException e)
			{
				throw new RuntimeException(e);
			}
		}
		return this.context;
	}

	private static Hashtable<String, String> getEnvironment(ServletContext servletContext) {
		Hashtable<String, String> environment = new Hashtable<String, String>(3);

		try
		{
			InitialContext context = new InitialContext();
			Context ctx = (Context) context.lookup("java:comp/env");
			environment.put(INITIAL_CONTEXT_FACTORY, (String) ctx.lookup(INITIAL_CONTEXT_FACTORY));
			environment.put(URL_PKG_PREFIXES, (String) ctx.lookup(URL_PKG_PREFIXES));
			environment.put(PROVIDER_URL, (String) ctx.lookup(PROVIDER_URL));
			return environment;
		}
		catch (NamingException e)
		{
			// Ignore
		}

		if (servletContext != null)
		{
			String icf = servletContext.getInitParameter(INITIAL_CONTEXT_FACTORY);
			String pkg = servletContext.getInitParameter(URL_PKG_PREFIXES);
			String url = servletContext.getInitParameter(PROVIDER_URL);

			if (icf != null && pkg != null && url != null)
			{
				environment.put(INITIAL_CONTEXT_FACTORY, icf);
				environment.put(URL_PKG_PREFIXES, pkg);
				environment.put(PROVIDER_URL, url);
				return environment;
			}
		}

		return null;
	}

	private final Config config;
	private Context context;
	private final HashMap<String, Object> cache = new HashMap<String, Object>();
	private static final ThreadLocal<ServiceFactory> instance = new ThreadLocal<ServiceFactory>();

	static final class Config
	{
		private Config(boolean remote, String postfix, String context, Hashtable<String, String> environment, String earName) {
			this.remote = remote;
			this.postfix = postfix;
			this.context = context;
			this.environment = environment;
			this.earName = earName;
		}

		private final boolean remote;
		private final String postfix;
		private final String context;
		private final Hashtable<String, String> environment;
		private final String earName;
	}

	public static final String CONFIG_KEY = "su.opencode.kefir.web.util.ServiceFactory.MODE";
	public static final String CONTEXT_KEY = "su.opencode.kefir.web.util.ServiceFactory.CONFIG";
	public static final String EAR_NAME_KEY = "su.opencode.kefir.web.util.ServiceFactory.EAR_NAME";

	public static final String LOCAL_REF_MODE = "local-ref";
	public static final String LOCAL_MODE = "local";
	public static final String REMOTE_MODE = "remote";

	protected static final Logger logger = Logger.getLogger(ServiceFactory.class);
}