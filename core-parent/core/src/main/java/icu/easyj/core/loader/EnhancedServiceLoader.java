/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.core.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import cn.hutool.system.JavaInfo;
import cn.hutool.system.SystemUtil;
import icu.easyj.core.executor.Initialize;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.MapUtils;
import icu.easyj.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * The type Enhanced service loader.
 * 注：从阿里的seata项目中复制过来的。
 *
 * @author slievrly
 */
public class EnhancedServiceLoader {

	/**
	 * Specify classLoader to load the service provider
	 *
	 * @param serviceClass the service class
	 * @param loader       the loader
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> serviceClass, ClassLoader loader) throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).load(loader);
	}

	/**
	 * load service provider
	 *
	 * @param serviceClass the service class
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> serviceClass) throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).load(findClassLoader());
	}

	/**
	 * load service provider
	 *
	 * @param serviceClass the service class
	 * @param activateName the activate name
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> serviceClass, String activateName) throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).load(activateName, findClassLoader());
	}

	/**
	 * Specify classLoader to load the service provider
	 *
	 * @param service      the service
	 * @param activateName the activate name
	 * @param loader       the loader
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> service, String activateName, ClassLoader loader)
			throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(service).load(activateName, loader);
	}

	/**
	 * Load service
	 *
	 * @param serviceClass the service
	 * @param activateName the activate name
	 * @param args         the args
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> serviceClass, String activateName, Object[] args)
			throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).load(activateName, args, findClassLoader());
	}

	/**
	 * Load service
	 *
	 * @param serviceClass the service
	 * @param activateName the activate name
	 * @param argsType     the args type
	 * @param args         the args
	 * @param <S>          the type of the service
	 * @return service the service
	 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
	 */
	public static <S> S load(Class<S> serviceClass, String activateName, Class<?>[] argsType, Object[] args)
			throws EnhancedServiceNotFoundException {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).load(activateName, argsType, args, findClassLoader());
	}

	/**
	 * get all implements
	 *
	 * @param serviceClass the service class
	 * @param <S>          the type of the service
	 * @return list the service list
	 */
	@NonNull
	public static <S> List<S> loadAll(Class<S> serviceClass) {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).loadAll(findClassLoader());
	}

	/**
	 * get all implements
	 *
	 * @param serviceClass the service
	 * @param argsType     the args type
	 * @param args         the args
	 * @param <S>          the type of the service
	 * @return list the service list
	 */
	public static <S> List<S> loadAll(Class<S> serviceClass, Class<?>[] argsType, Object[] args) {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).loadAll(argsType, args, findClassLoader());
	}

	/**
	 * Unload all.
	 */
	public static void unloadAll() {
		InnerEnhancedServiceLoader.removeAllServiceLoader();
	}

	/**
	 * Unload.
	 *
	 * @param <S>     the type parameter
	 * @param service the service
	 */
	public static <S> void unload(Class<S> service) {
		InnerEnhancedServiceLoader.removeServiceLoader(service);
	}

	/**
	 * Unload.
	 *
	 * @param <S>          the type parameter
	 * @param service      the service
	 * @param activateName the activate name
	 */
	public static <S> void unload(Class<S> service, String activateName) {
		if (activateName == null) {
			throw new IllegalArgumentException("activateName is null");
		}
		InnerEnhancedServiceLoader<S> serviceLoader = InnerEnhancedServiceLoader.getServiceLoader(service);
		ConcurrentMap<Class<?>, ExtensionDefinition> classToDefinitionMap = serviceLoader.classToDefinitionMap;
		List<ExtensionDefinition> extensionDefinitions = new ArrayList<>();
		for (Map.Entry<Class<?>, ExtensionDefinition> entry : classToDefinitionMap.entrySet()) {
			String name = entry.getValue().getName();
			if (null == name) {
				continue;
			}
			if (name.equals(activateName)) {
				extensionDefinitions.add(entry.getValue());
				classToDefinitionMap.remove(entry.getKey());
			}
		}
		serviceLoader.nameToDefinitionsMap.remove(activateName);
		if (!CollectionUtils.isEmpty(extensionDefinitions)) {
			for (ExtensionDefinition definition : extensionDefinitions) {
				serviceLoader.definitionToInstanceMap.remove(definition);

			}
		}

	}

	/**
	 * Get all the extension classes, follow {@linkplain LoadLevel} defined and sort order
	 *
	 * @param serviceClass the service class
	 * @param <S>          the type of the service
	 * @return all extension class
	 */
	static <S> List<Class<?>> getAllExtensionClass(Class<S> serviceClass) {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).getAllExtensionClass(findClassLoader());
	}

	/**
	 * Get all the extension classes, follow {@linkplain LoadLevel} defined and sort order
	 *
	 * @param serviceClass the service class
	 * @param loader       the loader
	 * @param <S>          the type of the service
	 * @return all extension class
	 */
	static <S> List<Class<?>> getAllExtensionClass(Class<S> serviceClass, ClassLoader loader) {
		return InnerEnhancedServiceLoader.getServiceLoader(serviceClass).getAllExtensionClass(loader);
	}

	/**
	 * Cannot use TCCL, in the pandora container will cause the class in the plugin not to be loaded
	 *
	 * @return loader the class loader
	 */
	private static ClassLoader findClassLoader() {
		return EnhancedServiceLoader.class.getClassLoader();
	}


	private static class InnerEnhancedServiceLoader<S> {
		private static final Logger LOGGER = LoggerFactory.getLogger(InnerEnhancedServiceLoader.class);
		private static final String SERVICES_DIRECTORY = "META-INF/services/";

		private static final ConcurrentMap<Class<?>, InnerEnhancedServiceLoader<?>> SERVICE_LOADERS =
				new ConcurrentHashMap<>();

		private final Class<S> type;
		private final Holder<List<ExtensionDefinition>> definitionsHolder = new Holder<>();
		private final ConcurrentMap<ExtensionDefinition, Holder<Object>> definitionToInstanceMap =
				new ConcurrentHashMap<>();
		private final ConcurrentMap<String, List<ExtensionDefinition>> nameToDefinitionsMap = new ConcurrentHashMap<>();
		private final ConcurrentMap<Class<?>, ExtensionDefinition> classToDefinitionMap = new ConcurrentHashMap<>();

		private InnerEnhancedServiceLoader(Class<S> type) {
			this.type = type;
		}

		/**
		 * Get the ServiceLoader for the specified Class
		 *
		 * @param type the type of the extension point
		 * @param <S>  the type
		 * @return the service loader
		 * @throws IllegalArgumentException if {@code type} is {@code null}
		 */
		private static <S> InnerEnhancedServiceLoader<S> getServiceLoader(Class<S> type) {
			if (type == null) {
				throw new IllegalArgumentException("Enhanced Service type is null");
			}
			return (InnerEnhancedServiceLoader<S>)MapUtils.computeIfAbsent(SERVICE_LOADERS, type,
					key -> new InnerEnhancedServiceLoader<>(type));
		}

		private static <S> InnerEnhancedServiceLoader<S> removeServiceLoader(Class<S> type) {
			if (type == null) {
				throw new IllegalArgumentException("Enhanced Service type is null");
			}
			return (InnerEnhancedServiceLoader<S>)SERVICE_LOADERS.remove(type);
		}

		private static void removeAllServiceLoader() {
			SERVICE_LOADERS.clear();
		}

		/**
		 * Specify classLoader to load the service provider
		 *
		 * @param loader the loader
		 * @return s
		 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
		 */
		private S load(ClassLoader loader) throws EnhancedServiceNotFoundException {
			return loadExtension(loader, null, null);
		}

		/**
		 * Specify classLoader to load the service provider
		 *
		 * @param activateName the activate name
		 * @param loader       the loader
		 * @return s
		 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
		 */
		private S load(String activateName, ClassLoader loader)
				throws EnhancedServiceNotFoundException {
			return loadExtension(activateName, loader, null, null);
		}

		/**
		 * Load s.
		 *
		 * @param activateName the activate name
		 * @param args         the args
		 * @param loader       the loader
		 * @return the s
		 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
		 */
		private S load(String activateName, Object[] args, ClassLoader loader)
				throws EnhancedServiceNotFoundException {
			Class<?>[] argsType = null;
			if (args != null && args.length > 0) {
				argsType = new Class[args.length];
				for (int i = 0; i < args.length; i++) {
					argsType[i] = args[i].getClass();
				}
			}
			return loadExtension(activateName, loader, argsType, args);
		}

		/**
		 * Load s.
		 *
		 * @param activateName the activate name
		 * @param argsType     the args type
		 * @param args         the args
		 * @param loader       the class loader
		 * @return the s
		 * @throws EnhancedServiceNotFoundException the enhanced service not found exception
		 */
		private S load(String activateName, Class<?>[] argsType, Object[] args, ClassLoader loader)
				throws EnhancedServiceNotFoundException {
			return loadExtension(activateName, loader, argsType, args);
		}

		/**
		 * get all implements
		 *
		 * @param loader the class loader
		 * @return list
		 */
		@NonNull
		private List<S> loadAll(ClassLoader loader) {
			return loadAll(null, null, loader);
		}

		/**
		 * get all implements
		 *
		 * @param argsType the args type
		 * @param args     the args
		 * @return list
		 */
		@NonNull
		private List<S> loadAll(Class<?>[] argsType, Object[] args, ClassLoader loader) {
			List<S> allInstances = new ArrayList<>();
			List<Class<?>> allClasses = getAllExtensionClass(loader);
			if (CollectionUtils.isEmpty(allClasses)) {
				return allInstances;
			}
			try {
				for (Class<?> clazz : allClasses) {
					ExtensionDefinition definition = classToDefinitionMap.get(clazz);
					allInstances.add(getExtensionInstance(definition, loader, argsType, args));
				}
			} catch (EnhancedServiceNotFoundException e) {
				throw e;
			} catch (RuntimeException e) {
				throw new EnhancedServiceNotFoundException("get extension instance failed", e);
			}
			return allInstances;
		}

		/**
		 * Get all the extension classes, follow {@linkplain LoadLevel} defined and sort order
		 *
		 * @param loader the loader
		 * @return all extension class
		 */
		private List<Class<?>> getAllExtensionClass(ClassLoader loader) {
			return loadAllExtensionClass(loader);
		}

		private S loadExtension(ClassLoader loader, Class<?>[] argTypes, Object[] args) {
			try {
				loadAllExtensionClass(loader);
				ExtensionDefinition defaultExtensionDefinition = getDefaultExtensionDefinition();
				return getExtensionInstance(defaultExtensionDefinition, loader, argTypes, args);
			} catch (EnhancedServiceNotFoundException e) {
				throw e;
			} catch (RuntimeException e) {
				throw new EnhancedServiceNotFoundException("not found service provider for: " + type.getName(), e);
			}
		}

		private S loadExtension(String activateName, ClassLoader loader, Class<?>[] argTypes,
								Object[] args) {
			if (StringUtils.isEmpty(activateName)) {
				throw new IllegalArgumentException("the name of service provider for [" + type.getName() + "] name is null");
			}
			try {
				loadAllExtensionClass(loader);
				ExtensionDefinition cachedExtensionDefinition = getCachedExtensionDefinition(activateName);
				return getExtensionInstance(cachedExtensionDefinition, loader, argTypes, args);
			} catch (EnhancedServiceNotFoundException e) {
				throw e;
			} catch (RuntimeException e) {
				throw new EnhancedServiceNotFoundException("not found service provider for: " + type.getName(), e);
			}
		}

		private S getExtensionInstance(ExtensionDefinition definition, ClassLoader loader, Class<?>[] argTypes,
									   Object[] args) {
			if (definition == null) {
				throw new EnhancedServiceNotFoundException("not found service provider for : " + type.getName());
			}
			if (Scope.SINGLETON.equals(definition.getScope())) {
				Holder<Object> holder = MapUtils.computeIfAbsent(definitionToInstanceMap, definition,
						key -> new Holder<>());
				Object instance = holder.get();
				if (instance == null) {
					synchronized (holder) {
						instance = holder.get();
						if (instance == null) {
							instance = createNewExtension(definition, loader, argTypes, args);
							holder.set(instance);
						}
					}
				}
				return (S)instance;
			} else {
				return createNewExtension(definition, loader, argTypes, args);
			}
		}

		private S createNewExtension(ExtensionDefinition definition, ClassLoader loader, Class<?>[] argTypes, Object[] args) {
			Class clazz = definition.getServiceClass();
			try {
				S newInstance = (S)initInstance(clazz, argTypes, args);
				return newInstance;
			} catch (Exception e) {
				throw new IllegalStateException("Extension instance(definition: " + definition + ", class: " +
						type + ")  could not be instantiated: " + e.getMessage(), e);
			}
		}

		private List<Class<?>> loadAllExtensionClass(ClassLoader loader) {
			List<ExtensionDefinition> definitions = definitionsHolder.get();
			if (definitions == null) {
				synchronized (definitionsHolder) {
					definitions = definitionsHolder.get();
					if (definitions == null) {
						definitions = findAllExtensionDefinition(loader);
						definitionsHolder.set(definitions);
					}
				}
			}
			return definitions.stream().map(ExtensionDefinition::getServiceClass).collect(Collectors.toList());
		}

		private List<ExtensionDefinition> findAllExtensionDefinition(ClassLoader loader) {
			List<ExtensionDefinition> extensionDefinitions = new ArrayList<>();
			try {
				loadFile(SERVICES_DIRECTORY, loader, extensionDefinitions);
			} catch (IOException e) {
				throw new EnhancedServiceNotFoundException(e);
			}

			//After loaded all the extensions,sort the caches by order
			if (!nameToDefinitionsMap.isEmpty()) {
				for (List<ExtensionDefinition> definitions : nameToDefinitionsMap.values()) {
					definitions.sort((def1, def2) -> {
						int o1 = def1.getOrder();
						int o2 = def2.getOrder();
						return Integer.compare(o1, o2);
					});
				}
			}

			if (!extensionDefinitions.isEmpty()) {
				extensionDefinitions.sort((definition1, definition2) -> {
					int o1 = definition1.getOrder();
					int o2 = definition2.getOrder();
					return Integer.compare(o1, o2);
				});
			}

			return extensionDefinitions;
		}

		private void loadFile(String dir, ClassLoader loader, List<ExtensionDefinition> extensions)
				throws IOException {
			String fileName = dir + type.getName();
			Enumeration<java.net.URL> urls;
			if (loader != null) {
				urls = loader.getResources(fileName);
			} else {
				urls = ClassLoader.getSystemResources(fileName);
			}
			if (urls != null) {
				while (urls.hasMoreElements()) {
					java.net.URL url = urls.nextElement();
					try (
							InputStreamReader isr = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
							BufferedReader reader = new BufferedReader(isr)) {
						String line;
						while ((line = reader.readLine()) != null) {
							final int ci = line.indexOf('#');
							if (ci >= 0) {
								line = line.substring(0, ci);
							}
							line = line.trim();
							if (line.length() > 0) {
								try {
									ExtensionDefinition extensionDefinition = getUnloadedExtensionDefinition(line, loader);
									if (extensionDefinition == null) {
										if (LOGGER.isDebugEnabled()) {
											LOGGER.debug("The same extension {} has already been loaded, skipped", line);
										}
										continue;
									}
									extensions.add(extensionDefinition);
								} catch (LinkageError | ClassNotFoundException e) {
									LOGGER.warn("Load [{}] class fail. {}", line, e.getMessage());
								}
							}
						}
					} catch (Throwable e) {
						LOGGER.warn("load clazz instance error: {}", e.getMessage());
					}
				}
			}
		}

		private ExtensionDefinition getUnloadedExtensionDefinition(String className, ClassLoader loader)
				throws ClassNotFoundException {
			//Check whether the definition has been loaded
			if (!isDefinitionContainsClazz(className, loader)) {
				Class<?> clazz = Class.forName(className, true, loader);
				String serviceName = null;
				int priority = 0;
				Scope scope = Scope.SINGLETON;
				LoadLevel loadLevel = clazz.getAnnotation(LoadLevel.class);
				if (loadLevel != null) {
					serviceName = loadLevel.name();
					priority = loadLevel.order();
					scope = loadLevel.scope();

					// 判断依赖的Java版本
					float dependOnMinJavaVersion = loadLevel.dependOnMinJavaVersion();
					float dependOnMaxJavaVersion = loadLevel.dependOnMaxJavaVersion();
					if (dependOnMinJavaVersion > 0 || dependOnMaxJavaVersion > 0) {
						JavaInfo javaInfo = SystemUtil.getJavaInfo();
						int version = (int)(javaInfo.getVersionFloat() * 10);
						if (dependOnMinJavaVersion > 0 && (int)(dependOnMinJavaVersion * 10) > version) {
							throw new ClassNotFoundException("java version is less than v" + dependOnMinJavaVersion);
						}
						if (dependOnMaxJavaVersion > 0 && (int)(dependOnMaxJavaVersion * 10) < version) {
							throw new ClassNotFoundException("java version is greater than v" + dependOnMaxJavaVersion);
						}
					}
				}
				ExtensionDefinition result = new ExtensionDefinition(serviceName, priority, scope, clazz);
				classToDefinitionMap.put(clazz, result);
				if (serviceName != null) {
					MapUtils.computeIfAbsent(nameToDefinitionsMap, serviceName, e -> new ArrayList<>())
							.add(result);
				}
				return result;
			}
			return null;
		}

		private boolean isDefinitionContainsClazz(String className, ClassLoader loader) {
			for (Map.Entry<Class<?>, ExtensionDefinition> entry : classToDefinitionMap.entrySet()) {
				if (!entry.getKey().getName().equals(className)) {
					continue;
				}
				if (Objects.equals(entry.getValue().getServiceClass().getClassLoader(), loader)) {
					return true;
				}
			}
			return false;
		}

		private ExtensionDefinition getDefaultExtensionDefinition() {
			List<ExtensionDefinition> currentDefinitions = definitionsHolder.get();
			return icu.easyj.core.util.CollectionUtils.getLast(currentDefinitions);
		}

		private ExtensionDefinition getCachedExtensionDefinition(String activateName) {
			List<ExtensionDefinition> definitions = nameToDefinitionsMap.get(activateName);
			return icu.easyj.core.util.CollectionUtils.getLast(definitions);
		}

		/**
		 * init instance
		 *
		 * @param implClazz the impl clazz
		 * @param argTypes  the arg types
		 * @param args      the args
		 * @return s
		 * @throws IllegalAccessException    the illegal access exception
		 * @throws InstantiationException    the instantiation exception
		 * @throws NoSuchMethodException     the no such method exception
		 * @throws InvocationTargetException the invocation target exception
		 */
		private S initInstance(Class<S> implClazz, Class<?>[] argTypes, Object[] args)
				throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
			S s;
			if (argTypes != null && args != null) {
				// Constructor with arguments
				Constructor<S> constructor = implClazz.getDeclaredConstructor(argTypes);
				s = type.cast(constructor.newInstance(args));
			} else {
				// default Constructor
				s = type.cast(implClazz.newInstance());
			}
			if (s instanceof Initialize) {
				((Initialize)s).init();
			}
			return s;
		}

		/**
		 * Helper Class for hold a value.
		 *
		 * @param <T>
		 */
		private static class Holder<T> {
			private volatile T value;

			private void set(T value) {
				this.value = value;
			}

			private T get() {
				return value;
			}
		}
	}
}
