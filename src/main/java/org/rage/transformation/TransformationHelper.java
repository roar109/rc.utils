package org.rage.transformation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hector.mendoza
 * @since 09/21/2015
 *
 * */
public class TransformationHelper {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final String MAP_VALUE_KEY = "value";
    private static final String MAP_TYPE_KEY = "type";
    private static final String MAP_DATE_FORMAT_KEY = "dateFormat";
    private static final String MAP_ETYPE_KEY = "eType";
    private static final String FITNESS_CLASS_PACKAGE = "com.fitness";

    /** List of attributes to exclude */
    private static final Map<String, Boolean> EXCLUDE_ATTRIBUTE_LIST = new HashMap<String, Boolean>() {
	private static final long serialVersionUID = 1L;

	{
	    put("serialVersionUID", Boolean.TRUE);
	}

    };

    /**
     * Represents transformListToListMap
     *
     * @param instanceList
     * @return <T> List <Map <String, Map <String, Object>>>
     * @since Sep 25, 2015
     *
     */
    public static <T> List<Map<String, Map<String, Object>>> transformListToListMap(
	    final List<T> instanceList) {
	final List<Map<String, Map<String, Object>>> mapList = new ArrayList<>(
		instanceList.size());

	for (final T instance : instanceList) {
	    mapList.add(transformObjectToMap(instance));
	}
	return mapList;
    }

    /**
     * Converts object attributes into a Map of String, String values. Ignoring
     * lists types and the fields passed in the excludeList list (See
     * TransformationHelper.EXCLUDE_ATTRIBUTE_LIST for details).
     *
     * @param instance
     *            bean to be converted to a Map
     *
     * @return Map<String, Map<String, Object>> map of attributes in string
     *         format
     */
    public static Map<String, Map<String, Object>> transformObjectToMap(
	    final Object instance) {
	return transformObjectToMap(instance, EXCLUDE_ATTRIBUTE_LIST);
    }

    /**
     * Converts object attributes into a Map of String, String values. Ignoring
     * lists types and the fields passed in the excludeList list.
     *
     * @param instance
     *            bean to be converted to a Map
     * @param excludeList
     *            Map of attributes you want to exclude. It just check the
     *            containsKey method for fast lookup.
     *
     * @return Map<String, Map<String, Object>> map of attributes in string
     *         format
     *
     * */
    public static Map<String, Map<String, Object>> transformObjectToMap(
	    final Object instance, final Map<String, Boolean> excludeList) {
	final Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>();
	transformObject(instance, excludeList, properties);
	return properties;
    }

    /**
     * Transform the given object into a HashMap.
     *
     * @param instance
     * @param excludeList
     * @param properties
     * @param prefix
     *            prefix to add in case is an object
     */
    private static void transformObject(final Object instance,
	    final Map<String, Boolean> excludeList,
	    final Map<String, Map<String, Object>> properties) {
	final Field[] fields = instance.getClass().getDeclaredFields();

	for (final Field field : fields) {
	    if (!excludeList.containsKey(field.getName())) {
		try {
		    // We use the getter to retrieve the value
		    final Object getterValue = getGetterMethod(field.getName(),
			    instance).invoke(instance);

		    final Map<String, Object> value = giveMeTheMap(getterValue);

		    if (value != null) {
			properties.put(field.getName(), value);
		    }
		} catch (final Exception e) {
		    /* Swallow the exception .... e.printStackTrace(); */
		}
	    }
	}
    }

    /**
     * Create a instance of the clazz, populate it with the metadata and add
     * them to the passed list.
     *
     * @param instances
     * @param objectList
     * @param clazz
     */
    public static <T> void populateObjectListWithProperties(
	    final List<T> instances,
	    final List<Map<String, Map<String, Object>>> objectList,
	    final Class<T> clazz) {
	if ((instances == null) || (objectList == null)) {
	    return;
	}

	for (final Map<String, Map<String, Object>> object : objectList) {

	    try {
		final T newInstance = clazz.newInstance();
		populateObjectWithProperties(newInstance, object);
		instances.add(newInstance);
	    } catch (final Exception e) {
		/* Swallow exceptions e.printStackTrace(); */
	    }
	}
    }

    /**
     * Pass each Map value to its bean-attribute map key setter method.
     *
     * @param instance
     * @param metadata
     *
     */
    public static void populateObjectWithProperties(final Object instance,
	    final Map<String, Map<String, Object>> metadata) {
	if (metadata == null) {
	    return;
	}

	for (final String attribute : metadata.keySet()) {
	    final Method setterMethod = getSetterMethod(attribute,
		    instance.getClass());

	    // We "suppose" that the null are not present in the attributes
	    // lists
	    try {
		setValuesToAttribute(instance, setterMethod,
			metadata.get(attribute));
	    } catch (final Exception e) {
		/* Swallow the exception e.printStackTrace(); */
	    }
	}
    }

    /**
     * Get the getter method for the passed field. We expect to have standard
     * bean nomenclature
     *
     * @param fieldName
     * @param instance
     * @return Method}
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private static Method getGetterMethod(final String fieldName,
	    final Object instance) throws NoSuchMethodException,
	    SecurityException {
	final String getterName = "get"
		+ fieldName.substring(0, 1).toUpperCase()
		+ fieldName.substring(1, fieldName.length());
	return instance.getClass().getMethod(getterName);
    }

    /**
     * Search into the object metadata for the setter method needed to set the
     * value. It supposes the clazz has the standard bean nomenclature methods.
     *
     * @param attributeName
     * @param clazz
     *
     * @return setterMethod
     */
    @SuppressWarnings("rawtypes")
    private static Method getSetterMethod(final String attributeName,
	    final Class clazz) {
	Method setterMethod = null;
	final String setterName = "set"
		+ attributeName.substring(0, 1).toUpperCase()
		+ attributeName.substring(1, attributeName.length());

	for (final Method method : clazz.getMethods()) {
	    if (method.getName().equals(setterName)) {
		setterMethod = method;
		break;
	    }
	}

	return setterMethod;
    }

    /**
     * Set the correct value to the given Method on the passed object.
     *
     * @param instance
     *            Object to be filled with the data
     * @param setterMethod
     *            Method to be called - is the setter usually.
     * @param value
     *            Map of properties
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValuesToAttribute(final Object instance,
	    final Method setterMethod, final Map<String, Object> value)
	    throws Exception {
	if (value == null) {
	    return;
	}

	final String type = (String) value.get(MAP_TYPE_KEY);

	if (Date.class.getCanonicalName().equals(type)
		|| java.sql.Date.class.getCanonicalName().equals(type)
		|| java.sql.Timestamp.class.getCanonicalName().equals(type)) {
	    String format = DATE_FORMAT;
	    if (value.containsKey(MAP_DATE_FORMAT_KEY)) {
		format = (String) value.get(MAP_DATE_FORMAT_KEY);
	    }
	    final SimpleDateFormat sfd = new SimpleDateFormat(format);
	    setterMethod.invoke(instance,
		    sfd.parse((String) value.get(MAP_VALUE_KEY)));

	} else if ((BigDecimal.class.getCanonicalName().equals(type))) {
	    setterMethod.invoke(instance,
		    new BigDecimal((String) value.get(MAP_VALUE_KEY)));

	} else if (type.startsWith(FITNESS_CLASS_PACKAGE)) {
	    final Object innerObjectInstance = Class.forName(type)
		    .newInstance();
	    setterMethod.invoke(instance, innerObjectInstance);
	    populateObjectWithProperties(innerObjectInstance,
		    (Map<String, Map<String, Object>>) value.get(MAP_VALUE_KEY));

	} else if (ArrayList.class.getCanonicalName().equals(type)
		|| Set.class.getCanonicalName().equals(type)) {
	    // Get the List<E> type from the metadata
	    final String eType = (String) value.get(MAP_ETYPE_KEY);
	    // Instantiate the destination list
	    final Object rawCollection = Class.forName(type).newInstance();
	    final Collection collection = (Collection) rawCollection;
	    // Get the List<Map> with the required meta data
	    final List<Map<String, Map<String, Object>>> listValues = (List<Map<String, Map<String, Object>>>) value
		    .get(MAP_VALUE_KEY);
	    final Iterator<Map<String, Map<String, Object>>> it = listValues
		    .iterator();

	    // Convert each of the List<Map> to the destination List<E> types.
	    while (it.hasNext()) {
		final Object rawEType = Class.forName(eType).newInstance();
		populateObjectWithProperties(rawEType, it.next());
		collection.add(rawEType);
	    }

	    setterMethod.invoke(instance, collection);
	} else {
	    setterMethod.invoke(instance, (String) value.get(MAP_VALUE_KEY));
	}
    }

    /**
     * Converts the given value to its string representation.
     *
     * @param value
     * @return metadata representation of the value object
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Object> giveMeTheMap(final Object value) {
	final Map<String, Object> metadata = new HashMap<String, Object>();
	Object objectValue = null;
	String classType = null;

	if ((value instanceof Date) || (value instanceof java.sql.Date)
		|| (value instanceof java.sql.Timestamp)) {
	    final SimpleDateFormat sfd = new SimpleDateFormat(DATE_FORMAT);
	    objectValue = sfd.format((Date) value);
	    metadata.put(MAP_DATE_FORMAT_KEY, DATE_FORMAT);
	    classType = Date.class.getCanonicalName();

	} else if (value.getClass().getPackage().getName()
		.contains(FITNESS_CLASS_PACKAGE)) {
	    final Map<String, Map<String, Object>> innerMetadata = new HashMap<String, Map<String, Object>>();
	    transformObject(value, EXCLUDE_ATTRIBUTE_LIST, innerMetadata);
	    objectValue = innerMetadata;

	} else if ((value instanceof List) || (value instanceof Set)) {
	    final Collection vlist = (Collection) value;
	    final List<Map<String, Map<String, Object>>> listValues = new ArrayList<Map<String, Map<String, Object>>>();
	    Map<String, Map<String, Object>> innerMetadata = null;
	    String eType = null;

	    // Convert each List<E> E element to its map representation
	    for (final Object element : vlist) {
		innerMetadata = new HashMap<String, Map<String, Object>>();
		transformObject(element, EXCLUDE_ATTRIBUTE_LIST, innerMetadata);
		listValues.add(innerMetadata);
		// We need the (List<E>) E type for instantiation when we
		// construct the object again.
		eType = element.getClass().getCanonicalName();
	    }
	    objectValue = listValues;
	    metadata.put(MAP_ETYPE_KEY, eType);
	} else {
	    objectValue = String.valueOf(value);
	}

	classType = classType != null ? classType : value.getClass()
		.getCanonicalName();

	metadata.put(MAP_TYPE_KEY, classType);
	metadata.put(MAP_VALUE_KEY, objectValue);

	return metadata;
    }

}