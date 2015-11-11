# rc.utils

Java Utils

### org.rage.filter.FieldsFilter 

Nullify the fields in an POJO array that we don't want to, i.e.:
    
    String fields = "name,email";
    String separator = ",";
    List<DummyVO> array = new ArrayList<DummyVO>();
	array.add(new DummyVO(1, "usernamevalue","emailvalue"));
    
    FieldsFilter.filterFields(fields, array, separator)

The `array` values have the `name` and `email` properties filled, other properties will be empty.

###  org.rage.transformation.TransformationHelper 

Converts any java object that follow the java beans convention to a Map<String, Map<String, Object> approach.

POJO:

    public class DummyVO{
    
       private Integer id;
       private String  username;
       private String  email;
    
    	... setters and getters
    }

Run:

    Map<String, Map<String, Object> map = org.rage.transformation.TransformationHelper.transformObjectToMap(new DummyVO(1,"someusername","someemail"));

Log output of the `map` variable:

    {id={value=1, type=java.lang.Integer}, username={value=someusername, type=java.lang.String}, email={value=someemail, type=java.lang.String}}

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
    				eType : "java.lang.Class of the  raw List<E> type in case this field is a list"
    			}
    ...
    }

This is recursive, if we have a pojo inside other pojo, this will describe it too, so the "value"  could be string|Map|List as possible values
