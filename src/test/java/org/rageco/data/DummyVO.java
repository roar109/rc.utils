package org.rageco.data;


/**
 * DummyVO represents ...
 * 
 * @author hector.mendoza
 * @since Jun 3, 2014
 * 
 */
public class DummyVO
{

   private Integer id;
   private String  username;
   private String  email;


   /**
    * Constructs an instance of DummyVO object.
    */
   public DummyVO ()
   {
      // Because i want to
   }


   /**
    * Constructs an instance of DummyVO object.
    * 
    * @param idValue
    * @param usernameValue
    * @param emailValue
    */
   public DummyVO (final Integer idValue, final String usernameValue, final String emailValue)
   {
      this.id = idValue;
      this.username = usernameValue;
      this.email = emailValue;
   }


   /**
    * @return the id
    */
   public Integer getId ()
   {
      return id;
   }


   /**
    * @param idValue the id to set
    */
   public void setId (final Integer idValue)
   {
      this.id = idValue;
   }


   /**
    * @return the username
    */
   public String getUsername ()
   {
      return username;
   }


   /**
    * @param usernameValue the username to set
    */
   public void setUsername (final String usernameValue)
   {
      this.username = usernameValue;
   }


   /**
    * @return the email
    */
   public String getEmail ()
   {
      return email;
   }


   /**
    * @param emailValue the email to set
    */
   public void setEmail (final String emailValue)
   {
      this.email = emailValue;
   }


   /**
    * Overrides toString
    * 
    * @return String
    * @since Jun 3, 2014
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString ()
   {
      final StringBuilder sb = new StringBuilder ();
      if (id != null)
      {
         sb.append (id);
      }
      if (username != null)
      {
         sb.append (username);
      }
      if (email != null)
      {
         sb.append (email);
      }
      return sb.toString ();
   }
}
