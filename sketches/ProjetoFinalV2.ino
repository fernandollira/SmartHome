#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#include <Ticker.h>
#include <math.h>

#define FIREBASE_HOST "smart-home-45b06.firebaseio.com"
#define FIREBASE_AUTH "lbBcm5b5ctISfVsKRwc1fVBELe6JoRmG52CNYX5B"
#define PUBLISH_INTERVAL 2000

//#define WIFI_SSID "Quaresma"
//#define WIFI_PASSWORD "15081967"

const char* ssid     = "Xablau";
const char* password = "fuderoso5";

//LEDS
const int ledPin = D8;
const int ledPin2 = D6;
const int ledPin3 = D7;
const int ventoinha = D0;

const int presenca = D1;

//So para saber se ligo ou nao o sensor de presenca
const int presenca2 = D4;

const int buzzer = D2;
const int LM35 = 0;
float temperatura = 0;
int sensor = 0;

Ticker ticker;
bool publishNewState = true;

void publish() {
  publishNewState = true;
}

void setup() {
  Serial.begin(115200);
  delay(10);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  pinMode(ledPin, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ventoinha, OUTPUT);
  pinMode(presenca2, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(presenca, INPUT);

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setBool("led", false);
  Firebase.setBool("led2", false);
  Firebase.setBool("led3", false);
  Firebase.setBool("presence", false);
  Firebase.setBool("presence_switch", false);
  Firebase.setBool("arCondicionado", false);

  // Registra o ticker para publicar de tempos em tempos
  ticker.attach_ms(PUBLISH_INTERVAL, publish);
}

void loop() {
  //isnan = returns 1 if "not a number"

  if (publishNewState) {
    Serial.println("Publish new State");

    temperatura = tempRead();
    Serial.print("Temperatura = ");
    Serial.print(temperatura);
    Serial.println(" *C");
    if (!isnan(temperatura)) {
      // Manda para o firebase
      Firebase.pushFloat("temperature", temperatura);
      publishNewState = false;
    } else {
      Serial.println("Error Publishing");
    }

    if (temperatura >= 28)
      digitalWrite(ventoinha, HIGH);
    else
      digitalWrite(ventoinha, LOW);
  }

  bool precenceValue = Firebase.getBool("presence_switch");
  digitalWrite(presenca2, precenceValue ? HIGH : LOW);

  int presence = digitalRead(presenca);
  Firebase.setBool("presence", presence == HIGH);

  if (precenceValue) {
    if (presence == HIGH)
      tone(buzzer, 300, 5000);
    else {
      digitalWrite(buzzer, LOW);
    }
  }

  bool arValue = Firebase.getBool("arCondicionado");
  digitalWrite(ventoinha, arValue ? HIGH : LOW);

  bool ledValue1 = Firebase.getBool("led");
  digitalWrite(ledPin, ledValue1 ? HIGH : LOW);

  bool ledValue2 = Firebase.getBool("led2");
  digitalWrite(ledPin2, ledValue2 ? HIGH : LOW);

  bool ledValue3 = Firebase.getBool("led3");
  digitalWrite(ledPin3, ledValue3 ? HIGH : LOW);

  Serial.println(Firebase.getBool("led"));
  Serial.println(Firebase.getBool("led2"));
  Serial.println(Firebase.getBool("led3"));

}

float tempRead() {

  sensor = analogRead(LM35);
  float millivolts = ( sensor / 1024.0) * 3000;
  temperatura = millivolts / 10;
  delay(500);

  return temperatura;

}
