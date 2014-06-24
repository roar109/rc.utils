package org.rageco.filter;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper that filter the amount of fields in a given object, just put to null its values via set and get methods.
 * 
 * @author <a href="mailto:roar109@gmail.com">Hector Mendoza</a>
 * @version $Id$
 * @since Apr 30, 2014
 * 
 */
public final class FieldsFilter
{

   private final static String                   DEFAULT_FIELD_SEPARATOR = ",";
   private final static HashMap <String, String> classesAvoid            = new HashMap <String, String> ()
                                                                         {
                                                                            {
                                                                               put ("class", "");
                                                                            }
                                                                            /** long for serialVersionUID */
                                                                            private static final long serialVersionUID = 1L;

                                                                         };


   private FieldsFilter ()
   {
      // Avoid instantiation
   }


   /**
    * Method that put null in each field that not is listed into the fields String
    * 
    * @param <T>
    * @param fields String of fields separated by "," or other character
    * @param list
    * @param separator A separator, if is null is ","
    * @return list
    * @since Apr 30, 2014
    * 
    */
   public static <T> List <T> filterFields (final String fields, final List <T> list, final String separator)
   {
      if (StringUtils.isNotBlank (fields))
      {
         String fieldsSeparator = DEFAULT_FIELD_SEPARATOR;
         if (StringUtils.isNotBlank (separator))
         {
            fieldsSeparator = separator;
         }
         filterFieldsInternal (fields, list, fieldsSeparator);
      }
      return list;
   }


   /**
    * Method that will filter the fields to display
    * 
    * @param fields Fields separated with comma. It makes a trim so, spaces end and begin will be removed.
    * @param list
    * */
   private static <T> void filterFieldsInternal (final String fieldsStr, final List <T> clubs, final String separator)
   {
      try
      {
         // Avoid that null's on BigDecimal fields throws and exception with Apache Utils.
         BeanUtilsBean.getInstance ().getConvertUtils ().register (new BigDecimalConverter (), BigDecimal.class);

         final String[] fields = StringUtils.split (fieldsStr, separator);
         Map <String, String> classFields = null;
         // We need to get the type of the T, so we get the first element and create an instance to retrieve all the
         // fields.
         classFields = BeanUtils.describe (clubs.get (0).getClass ().newInstance ());
         final int initialFieldCount = classFields.size ();

         // Remove from map the field that we want to keep, anything else will set to null.
         for (String field : fields)
         {
            field = String.valueOf (field).trim ();
            if (classFields.containsKey (field))
            {
               classFields.remove (field);
            }
         }

         // If the size is different you have fields to set to null, if is the same no property will be removed.
         if (initialFieldCount != classFields.size ())
         {
            for (final T cdDto : clubs)
            {
               checkNullFields (classFields, cdDto);
            }
         }
      }
      catch (final Exception e)
      {
         e.printStackTrace ();
      }
   }


   /**
    * Method that set null to the availableFields fields
    * 
    * @param availableFields list of fields on the Object to set to null
    * @param T
    * @throws NoSuchMethodException
    * */
   private static <T> void checkNullFields (final Map <String, String> availableFields, final T club)
         throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
   {
      for (final String field : availableFields.keySet ())
      {
         // Avoid properties like class
         if ( !classesAvoid.containsKey (field))
         {
            // Invoke null
            PropertyUtils.setProperty (club, field, null);
         }
      }
   }
}
