#include <jni.h>
#include <string>

extern "C"
jstring
Java_sahilguptalive_com_androiddemos_HomeActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
