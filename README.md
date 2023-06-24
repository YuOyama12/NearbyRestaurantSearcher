# NearbyRestaurantSearcher

## 概要
GPS機能を用いて現在地を取得し、その現在地周辺にあるレストランを検索し表示してくれるアプリです。
ホットペッパー Webサービスにより提供されるAPI、Google Maps PlatformのAPIを使用しています。

Android Studioで利用する際には、"local.properties"内に以下を、お持ちのAPIキーを入れて、入力してください。
```
# Your own API key for "HOTPEPPER"
apiKey=""

# Your own API Key for "Google Map Platform"
MAPS_API_KEY=""
```

## 簡単な使い方紹介

1. スライダーから検索範囲を設定し、「店舗検索」ボタンを押して、付近のお店を検索。（初回起動時には、アプリに位置情報の使用を許可してください。）

   ![usage1](https://github.com/YuOyama12/NearbyRestaurantSearcher/assets/94959504/d8a7f06a-4bc4-442f-96f3-037196720ca1)
- 「検索条件設定」ボタンを押すことで、地図を非表示にしてリストの見える範囲を広げることも可能です。

   ![usage1-a](https://github.com/YuOyama12/NearbyRestaurantSearcher/assets/94959504/588cbc27-906b-4523-9c59-78acb6ffa44b)

2. 検索結果が、GoogleMap上、そしてリスト上に表示されます。気になるお店をクリック、または長押しすることで、詳細画面に飛ぶことが可能です。（Mapに場所が表示されている場合は、地図が一度その場所にズームされます。もう一度クリックすると詳細画面に飛びます。）

   ![usage2](https://github.com/YuOyama12/NearbyRestaurantSearcher/assets/94959504/7ab8d8c7-1875-4a52-aced-eeee6100cd24)

3. 詳細画面では、住所や開店時間等より詳しい情報を確認することができます。「ホットペッパーのページに移動」ボタンを押すことで、ホットペッパーのWEBサイトに移動することができます。

   ![usage3](https://github.com/YuOyama12/NearbyRestaurantSearcher/assets/94959504/e4a8e12b-c5c3-4b4e-a656-be27df98cbb8)

## 主な使用技術・ライブラリ等
- Kotlin
- Jetpack Compose
    - viewModel
    - Navigation
- Hilt
- Retrofit2
- Picasso
- [ホットペッパー Webサービス](http://webservice.recruit.co.jp/)
- [Google Maps Platform](https://mapsplatform.google.com/?hl=ja&_gl=1*2kx61n*_ga*MTQyMTQxNzY1Mi4xNjg3NTY4NDY2*_ga_NRWSTWS78N*MTY4NzU2ODQ2Ni4xLjEuMTY4NzU2OTY0MC4wLjAuMA..)

## 搭載した主な機能
- 自分の現在地とその周囲を、GoogleMap上で確認できる機能
- 周囲の検索範囲をスライダーで変更できるようにし、Map上でもその範囲を確認できる機能
- 検索されたお店の各位置を、Map上に表示する機能
- 検索されたお店の情報を、リスト上に表示する機能
- 検索条件を予算、ジャンルの２つより絞り込む機能
- お店リストのページング機能（表示される順番は現在地から近い順のみとなっています。）
- リスト上に表示された項目をタップすることで、詳細画面に遷移する機能
- 詳細画面内から、ホットペッパーの該当ページに移動することができる機能