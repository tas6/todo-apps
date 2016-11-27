# todo-apps

## curlコマンドサンプル

### 照会

#### 1件

```sh
curl https://api.example.com/v1/tasks/1 \
--verbose \
--request GET \
--header "Authorization: Basic $(echo -n "user:pass" | base64)" \
```

#### 全件

```sh
curl https://api.example.com/v1/tasks/ \
--verbose \
--request GET \
--header "Authorization: Basic $(echo -n "user:pass" | base64)" \
```

### 登録

```sh
curl https://api.example.com/v1/tasks/ \
--verbose \
--request POST \
--header "Authorization: Basic $(echo -n "user:pass" | base64)" \
--header 'Content-Type: application/json' \
--data-binary "{\"subject\":\"件名\", \"description\":\"内容\"}"
```

### 更新

```sh
curl https://api.example.com/v1/tasks/1 \
--verbose \
--request PUT \
--header "Authorization: Basic $(echo -n "user:pass" | base64)" \
--header 'Content-Type: application/json' \
--data-binary "{\"subject\":\"new件名\", \"description\":\"new内容\", \"done\": \"true\", \"version\":1}"
```

### 削除

```sh
curl https://api.example.com/v1/tasks/2 \
--verbose \
--request DELETE \
--header "Authorization: Basic $(echo -n "user:pass" | base64)" \
```
