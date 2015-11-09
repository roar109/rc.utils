# rc.utils

Java Utils

### org.rage.filter.FieldsFilter 

Nullify the fields in an POJO array that we don't want to, i.e.:
    
    String fields = "name,email";
    String separator = ",";
    List<DummyVO> array = new ArrayList<DummyVO>();
	array.add(new DummyVO(1, "usernamevalue","emailvalue"));
    
    FieldsFilter.filterFields(fields, array, separator)

`array` values with only the `name` and `email` fields will have values.

###  org.rage.transformation.TransformationHelper 

Converts any java object that follow the java beans convention to a Map<String, Map<String, Object> approach

POJO:

    public class DummyVO{
    
       private Integer id;
       private String  username;
       private String  email;
    
    	... setters and getters
    }

Output:

Map<String, Map<String, Object>:

    {id={value=1, type=java.lang.Integer}, username={value=someusername, type=java.lang.String}, email={value=somepássword, type=java.lang.String}}

Format:

    {
    field-name : {
    				value : "field value as string",
    				type : "java.lang.Class"
    			},
    field-name2 : {
    				value : "field value 2 as string",
    				type : "java.lang.Class",
    				dateFormat : "date format type used if is a date",
    				eType : "java.lang.class of the List<E> in case this field is a list"
    			}
    ...
    }
