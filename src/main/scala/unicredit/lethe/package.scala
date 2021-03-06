/* Copyright 2016-2019 UniCredit S.p.A.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package unicredit

package object lethe {
  def pow(n: Int, k:Int): Int = k match {
    case kk if kk < 0  => 0  // error
    case 0 => 1
    case 1 => n
    case 2 => n*n
    case kk  if kk % 2 == 0  => pow(pow(n, k/2), 2)
    case _  => pow(n, k/2)*pow(n, (k+1)/2)
  }
}