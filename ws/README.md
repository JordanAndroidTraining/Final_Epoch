# Epoch Webservice
## Features

* **Get resource**
  * ``http://localhost:8080/resources/{resource id}``
  * Parameters
    * resource id
```
Example: 
  GET http://localhost:8080/resources/2250
```

* **Get resource list by type**
  * ``http://localhost:8080/resources?type={type}``
  * Parameters
    * type: (CountrySide, FarmVisit, Spot, Aborigines)
```
Example: 
  GET http://localhost:8080/resources?type=CountrySide
```

* **Search**
  * ``http://localhost:8080/resources?keyword={keyword}&type={type}``
  * Parameters
    * keyword: search keyword
    * type: (CountrySide, FarmVisit, Spot, Aborigines)
```
Example: 
  GET http://localhost:8080/resources/search?keyword=娃娃谷民宿
  GET localhost:8080/resources/search?keyword=娃娃谷民宿&type=Aborigines
```

* **Comment**
  * ``http://localhost:8080/resources/2250/comments``
  * Parameters
    * subject
    * comment
    * imageUrl
    * rating
```
Example: 
  curl -X POST -d 'subject=subject&comment=comment&imageUrl=xxx&rating=5' http://localhost:8080/resources/2250/comments
```

* **Resource ImageUrl**
  * ``http://localhost:8080/resources/2250/imageUrl``
  * Parameters
    * imageUrl
```
Example: 
curl -X POST -d ‘imageUrl=http://xxx.com/' http://localhost:8080/resources/2250/imageUrl
```
