# DiaryApp

Bu proje, kullanıcılara kendi günlüklerini tutma ve Firebase ile güvenli bir şekilde verilerini saklama imkanı sağlayan bir Android uygulamasıdır. 

Kullanıcılar uygulama üzerinden günlüklerini oluşturabilir, düzenleyebilir ve silebilirler. 

Günlük verileri Firebase Realtime Database'de saklanmakta ve kimlik doğrulama (Firebase Authentication) sayesinde kullanıcıların kişisel bilgileri güvende tutulmaktadır.

## Özellikler
- Firebase Entegrasyonu: Kullanıcıların verileri güvenli bir şekilde bulutta saklanır.
- Kimlik Doğrulama: Firebase Authentication ile kullanıcı giriş ve kayıt işlemleri.
- Günlük Yönetimi: Kullanıcıların günlük yazıp kaydedebileceği, düzenleyebileceği ve silebileceği bir yapı.
- Tasarım: Modern ve kullanıcı dostu bir arayüz.
- Veri Sürekliliği: Veriler bulutta saklandığından uygulama silinse bile kullanıcı verileri kaybolmaz.

## Kullanılan Teknolojiler
- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database

## Gereksinimler
- Android Studio Arctic Fox veya daha yeni bir sürüm
- Android Studio: Minimum versiyon Arctic Fox veya üstü.
- JDK: 1.8 veya üstü.
- Android SDK: 26 ve üstü.
- Kotlin: 1.8 veya üstü

## Kurulum

Uygulamayı çalıştırmak için aşağıdaki adımları izleyin:

Firebase Yapılandırması:
 - Firebase Console'a gidin ve yeni bir proje oluşturun.
 - Android uygulamanızı Firebase projenize ekleyin.
 - Firebase Console'dan `google-services.json` dosyasını indirin.
 - Bu dosyayı projenizin `app/` dizinine yerleştirin.




