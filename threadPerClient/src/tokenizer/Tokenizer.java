package tokenizer;

import java.io.IOException;
import java.io.InputStreamReader;

import tokenizer_http.HttpMessage;

public interface Tokenizer<T> {

   /**
    * Get the next complete message if it exists, advancing the tokenizer to the next message.
    * @return the next complete message, and null if no complete message exist.
 * @throws IOException 
    */
   HttpMessage nextMessage() throws IOException;
   
   /**
    * @return whether the input stream is still alive.
    */
   boolean isAlive();

   /**
    * adding a bufferedReader from which the tokenizer reads the input.
    */
   void addInputStream(InputStreamReader inputStreamReader);
}
