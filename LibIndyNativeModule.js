//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { LibIndy } = NativeModules

export default {
  exampleMethod () {
    console.log("Ran JS wrapper!");
    return LibIndy.exampleMethod()
  },

  EXAMPLE_CONSTANT: LibIndy.EXAMPLE_CONSTANT
}
