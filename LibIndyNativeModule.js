//  Created by react-native-create-bridge

import { NativeModules } from 'react-native'

const { LibIndy } = NativeModules

export default {
  exampleMethod () {
    return LibIndy.exampleMethod()
  },

  EXAMPLE_CONSTANT: LibIndy.EXAMPLE_CONSTANT
}
