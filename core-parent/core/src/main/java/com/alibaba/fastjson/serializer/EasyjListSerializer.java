/*
 * Copyright 2021-2024 the original author or authors.
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
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class EasyjListSerializer implements ObjectSerializer {

	public static final EasyjListSerializer INSTANCE = new EasyjListSerializer();

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {

		boolean writeClassName = serializer.out.isEnabled(SerializerFeature.WriteClassName)
				|| SerializerFeature.isEnabled(features, SerializerFeature.WriteClassName);

		SerializeWriter out = serializer.out;

		Type elementType = null;
		if (writeClassName) {
			elementType = TypeUtils.getCollectionItemType(fieldType);
		}

		if (object == null) {
			out.writeNull(SerializerFeature.WriteNullListAsEmpty);
			return;
		}

		List<?> list = (List<?>)object;

		if (list.size() == 0) {
			out.append("[]");
			return;
		}

		SerialContext context = serializer.getContext();
		serializer.setContext(context, object, fieldName, 0);

		ObjectSerializer itemSerializer;
		try {
			if (out.isEnabled(SerializerFeature.PrettyFormat)) {
				out.append('[');
				serializer.incrementIndent();

				int i = 0;
				for (Object item : list) {
					if (i != 0) {
						out.append(',');
					}

					serializer.println();
					if (item != null) {
						if (serializer.containsReference(item)) {
							serializer.writeReference(item);
						} else {
							itemSerializer = serializer.getObjectWriter(item.getClass());
							SerialContext itemContext = new SerialContext(context, object, fieldName, 0, 0);
							serializer.setContext(itemContext);
							itemSerializer.write(serializer, item, i, elementType, features);
						}
					} else {
						serializer.out.writeNull();
					}
					i++;
				}

				serializer.decrementIdent();
				serializer.println();
				out.append(']');
				return;
			}

			out.append('[');
			for (int i = 0, size = list.size(); i < size; ++i) {
				Object item = list.get(i);
				if (i != 0) {
					out.append(',');
				}

				if (item == null) {
					out.append("null");
				} else {
					Class<?> clazz = item.getClass();

					if (clazz == Integer.class) {
						out.writeInt((Integer)item);
					} else if (clazz == Long.class) {
						ObjectSerializer writer = serializer.config.getObjectWriter(clazz);
						try {
							writer.write(serializer, item, null, null, 0);
						} catch (IOException e) {
							throw new JSONException(e.getMessage(), e);
						}
					} else {
						if ((SerializerFeature.DisableCircularReferenceDetect.mask & features) != 0) {
							itemSerializer = serializer.getObjectWriter(item.getClass());
							itemSerializer.write(serializer, item, i, elementType, features);
						} else {
							if (!out.disableCircularReferenceDetect) {
								serializer.context = new SerialContext(context, object, fieldName, 0, 0);
							}

							if (serializer.containsReference(item)) {
								serializer.writeReference(item);
							} else {
								itemSerializer = serializer.getObjectWriter(item.getClass());
								if ((SerializerFeature.WriteClassName.mask & features) != 0
										&& itemSerializer instanceof JavaBeanSerializer) {
									JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer)itemSerializer;
									javaBeanSerializer.writeNoneASM(serializer, item, i, elementType, features);
								} else {
									itemSerializer.write(serializer, item, i, elementType, features);
								}
							}
						}
					}
				}
			}
			out.append(']');
		} finally {
			serializer.context = context;
		}
	}
}
