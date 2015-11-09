package org.rageco.filter.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.rageco.data.DummyVO;
import org.rageco.filter.FieldsFilter;

import java.util.ArrayList;
import java.util.List;


/**
 * FieldFilterTest represents ...
 * 
 * @author hector.mendoza
 * @since Jun 3, 2014
 * 
 */
public class FieldFilterTest
{

   /** FieldFilterTest for dummyList */
   private List <DummyVO> dummyList = null;


   /**
    * Represents config
    * 
    * @since Jun 3, 2014
    * 
    */
   @Before
   public void config ()
   {
      dummyList = new ArrayList <DummyVO> ();
      dummyList.add (new DummyVO (1, "Yo", "yo@mail.com"));
      dummyList.add (new DummyVO (2, "Foo", "foo@mail.com"));
      dummyList.add (new DummyVO (3, "Foobar", "foobar@mail.com"));
   }


   /**
    * If you pass a valid field name.
    * 
    * @since Jun 3, 2014
    * 
    */
   @Test
   public void testFieldsFilter ()
   {
      final String fields = "username";
      final List <DummyVO> filterFields = FieldsFilter.filterFields (fields, dummyList, null);

      assertEquals ("Actual size", 3, filterFields.size ());
      assertEquals ("Only Yo name", "Yo", filterFields.get (0).toString ());
      assertEquals ("Only Foo name", "Foo", filterFields.get (1).toString ());
      assertEquals ("Only Foobar name", "Foobar", filterFields.get (2).toString ());
      assertNull ("ID must be null", filterFields.get (0).getId ());
      assertNull ("Email should be null", filterFields.get (0).getEmail ());
   }


   /**
    * Test if you pass a wrong property it returns all the fields.
    * 
    * @since Jun 3, 2014
    * 
    */
   @Test
   public void testFieldsFilterWrongProperty ()
   {
      final String fields = "password";
      final List <DummyVO> filterFields = FieldsFilter.filterFields (fields, dummyList, null);
      assertEquals ("Actual size", 3, filterFields.size ());
      assertEquals ("Only Yo name", "1Yoyo@mail.com", filterFields.get (0).toString ());
   }
}
