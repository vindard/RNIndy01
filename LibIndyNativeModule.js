//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { LibIndy } = NativeModules

export default {
  async exampleMethod () {
    console.log("Ran JS wrapper!");
    const text = await LibIndy.exampleMethod();
    console.log(text);
  },

  EXAMPLE_CONSTANT: LibIndy.EXAMPLE_CONSTANT
}
