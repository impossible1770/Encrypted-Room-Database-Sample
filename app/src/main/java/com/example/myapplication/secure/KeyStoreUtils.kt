import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyStoreUtils {
    private const val ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore"
    private const val KEY_ALIAS = "my_key_alias"
    private const val KEYSTORE_TYPE = "KeyStoreType" // e.g., "PKCS12"

    fun getOrCreateAESKeyFromKeyStore(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_PROVIDER)
        keyStore.load(null)

        if (keyStore.containsAlias(KEY_ALIAS)) {
            // Key already exists, retrieve it from the keystore
            return keyStore.getKey(KEY_ALIAS, null) as SecretKey
        } else {
            // Key doesn't exist, generate a new AES key and store it in the keystore
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE_PROVIDER
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setKeySize(256)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            val secretKey = keyGenerator.generateKey()

            return secretKey
        }
    }
}