/**
 * Copyright 2015 YourLocalFax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.fudev.laye.internal.values;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.fudev.laye.internal.ValueType;

public class LayeStream extends LayeValue
{
   public final Stream<LayeValue> stream;
   
   public LayeStream(final Stream<LayeValue> stream)
   {
      super(ValueType.STREAM);
      this.stream = stream;
   }
   
   @Override
   public int hashCode()
   {
      // TODO hashCode
      return 0;
   }
   
   @Override
   public boolean equalTo_b(final LayeValue other)
   {
      return other == this;
   }
   
   @Override
   public String asstring()
   {
      return "stream";
   }
   
   public Stream<LayeValue> filter(final Predicate<? super LayeValue> predicate)
   {
      return stream.filter(predicate);
   }
   
   public Stream<LayeValue> map(final Function<? super LayeValue, ? extends LayeValue> function)
   {
      return stream.map(function);
   }
   
   public void forEach(final Consumer<? super LayeValue> consumer)
   {
      stream.forEach(consumer);
   }
   
   public LayeValue min(final Comparator<? super LayeValue> consumer)
   {
      final Optional<LayeValue> result = stream.min(consumer);
      if (result.isPresent())
      {
         return result.get();
      }
      return LayeValue.NULL;
   }
   
   public LayeValue max(final Comparator<? super LayeValue> consumer)
   {
      final Optional<LayeValue> result = stream.max(consumer);
      if (result.isPresent())
      {
         return result.get();
      }
      return LayeValue.NULL;
   }
}
