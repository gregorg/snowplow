/*
 * Copyright (c) 2013-2014 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0, and
 * you may not use this file except in compliance with the Apache License
 * Version 2.0.  You may obtain a copy of the Apache License Version 2.0 at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Apache License Version 2.0 is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the Apache License Version 2.0 for the specific language
 * governing permissions and limitations there under.
 */
package com.snowplowanalytics.snowplow
package enrich.kinesis
package bad

// Commons Codec
import org.apache.commons.codec.binary.Base64

// Specs2
import org.specs2.mutable.Specification

// This project
import SpecHelpers._

object InvalidEnrichedEventSpec {

  val raw = "CCgABAAABQ9qNGa4LABQAAAAQc3NjLTAuMC4xLVN0ZG91dAsAHgAAAAVVVEYtOAsAKAAAAAgxMC4wLjIuMgwAKQgAAQAAAAEIAAIAAAABCwADAAACeWU9dWUmdWVfbmE9Vmlld2VkK1Byb2R1Y3QmdWVfcHI9JTdCJTIycHJvZHVjdF9pZCUyMjolMjJBU08wMTA0MyUyMiwlMjJjYXRlZ29yeSUyMjolMjJEcmVzc2VzJTIyLCUyMmJyYW5kJTIyOiUyMkFDTUUlMjIsJTIycmV0dXJuaW5nJTIyOnRydWUsJTIycHJpY2UlMjI6NDkuOTUsJTIyc2l6ZXMlMjI6JTVCJTIyeHMlMjIsJTIycyUyMiwlMjJsJTIyLCUyMnhsJTIyLCUyMnh4bCUyMiU1RCwlMjJhdmFpbGFibGVfc2luY2UkZHQlMjI6MTU4MDElN0QmZHRtPTEzOTA5NDExMTUyNjMmdGlkPTY0NzYxNSZ2cD0yNTYweDk2MSZkcz0yNTYweDk2MSZ2aWQ9OCZkdWlkPTNjMTc1NzU0NGUzOWJjYTQmcD1tb2ImdHY9anMtMC4xMy4xJmZwPTI2OTU5MzA4MDMmYWlkPUNGZTIzYSZsYW5nPWVuLVVTJmNzPVVURi04JnR6PUV1cm9wZS9Mb25kb24mdWlkPWFsZXgrMTIzJmZfcGRmPTAmZl9xdD0xJmZfcmVhbHA9MCZmX3dtYT0wJmZfZGlyPTAmZl9mbGE9MSZmX2phdmE9MCZmX2dlYXJzPTAmZl9hZz0wJnJlcz0yNTYweDE0NDAmY2Q9MjQmY29va2llPTEmdXJsPWZpbGU6Ly9maWxlOi8vL1VzZXJzL2FsZXgvRGV2ZWxvcG1lbnQvZGV2LWVudmlyb25tZW50L2RlbW8vMS10cmFja2VyL2V2ZW50cy5odG1sL292ZXJyaWRkZW4tdXJsLwALAC0AAAAJbG9jYWxob3N0CwAyAAAAUU1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwLjk7IHJ2OjI2LjApIEdlY2tvLzIwMTAwMTAxIEZpcmVmb3gvMjYuMA8ARgsAAAAHAAAAFkNvbm5lY3Rpb246IGtlZXAtYWxpdmUAAAJwQ29va2llOiBfX3V0bWE9MTExODcyMjgxLjg3ODA4NDQ4Ny4xMzkwMjM3MTA3LjEzOTA4NDg0ODcuMTM5MDkzMTUyMS42OyBfX3V0bXo9MTExODcyMjgxLjEzOTAyMzcxMDcuMS4xLnV0bWNzcj0oZGlyZWN0KXx1dG1jY249KGRpcmVjdCl8dXRtY21kPShub25lKTsgX3NwX2lkLjFmZmY9Yjg5YTZmYTYzMWVlZmFjMi4xMzkwMjM3MTA3LjYuMTM5MDkzMTU0NS4xMzkwODQ4NjQxOyBoYmxpZD1DUGpqdWh2RjA1emt0UDdKN001Vm8zTklHUExKeTFTRjsgb2xmc2s9b2xmc2s1NjI5MjM2MzU2MTc1NTQ7IF9fdXRtYz0xMTE4NzIyODE7IHdjc2lkPXVNbG9nMVFKVkQ3anVoRlo3TTVWb0JDeVBQeWlCeVNTOyBfb2tsdj0xMzkwOTMxNTg1NDQ1JTJDdU1sb2cxUUpWRDdqdWhGWjdNNVZvQkN5UFB5aUJ5U1M7IF9vaz05NzUyLTUwMy0xMC01MjI3OyBfb2tiaz1jZDQlM0R0cnVlJTJDdmk1JTNEMCUyQ3ZpNCUzRDEzOTA5MzE1MjExMjMlMkN2aTMlM0RhY3RpdmUlMkN2aTIlM0RmYWxzZSUyQ3ZpMSUzRGZhbHNlJTJDY2Q4JTNEY2hhdCUyQ2NkNiUzRDAlMkNjZDUlM0Rhd2F5JTJDY2QzJTNEZmFsc2UlMkNjZDIlM0QwJTJDY2QxJTNEMCUyQzsgc3A9NzVhMTM1ODMtNWM5OS00MGUzLTgxZmMtNTQxMDg0ZGZjNzg0AAAAHkFjY2VwdC1FbmNvZGluZzogZ3ppcCwgZGVmbGF0ZQAAABpBY2NlcHQtTGFuZ3VhZ2U6IGVuLVVTLCBlbgAAACtBY2NlcHQ6IGltYWdlL3BuZywgaW1hZ2UvKjtxPTAuOCwgKi8qO3E9MC41AAAAXVVzZXItQWdlbnQ6IE1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwLjk7IHJ2OjI2LjApIEdlY2tvLzIwMTAwMTAxIEZpcmVmb3gvMjYuMAAAABRIb3N0OiBsb2NhbGhvc3Q6NDAwMQsAUAAAACQ3NWExMzU4My01Yzk5LTQwZTMtODFmYy01NDEwODRkZmM3ODQA"
}

class InvalidEnrichedEventSpec extends Specification {

  // TODO: update this after https://github.com/snowplow/snowplow/issues/463
  "Scala Kinesis Enrich" should {

    "return None for a valid SnowplowRawEvent which fails enrichment" in {

      val rawEvent = Base64.decodeBase64(InvalidEnrichedEventSpec.raw)

      val enrichedEvent = TestSource.enrichEvent(rawEvent)
      enrichedEvent must beNone
    }
  }
}
