# CategolJ2 Backend

[![Build Status](https://travis-ci.org/making/categolj2-backend.svg)](https://travis-ci.org/making/categolj2-backend)

CategoljJ2 is a micro blog system. This backend provides REST API for blog system and admin console to manage these data. You can create any blog frontend using this APIs ;)

![categolj](./logo.png)

The following technologies are used.

* Java 8
* Spring Boot 1.2
  * Spring 4.1
  * Spring MVC
  * Spring Security OAuth
  * Spring Data JPA
  * JPA 2.1
  * Flyway
  * ThymeLeaf
* Hibernate Search
* Backbone.js
* ...

 
## Normal launch

Download categolj2-backend.jar (0.20.0+) from [release page](https://github.com/making/categolj2-backend/releases).
This jar is executable by Spring Boot.

**Note that CategoLJ2 requires Java8**.

### Simple usage

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar --server.port 8080

Access

* Backend 
  * [http://localhost:8080/admin](http://localhost:8080/admin)
  * login with initial account (admin/demo)
* Sample frontend 
  * [http://localhost:8080](http://localhost:8080)
  * see [Setup frontend](#setup-frontend) to customize frontend screen.


CategoLJ2 supports H2 and MySQL and embedded H2 is used as default.

You can change H2 data location as following:

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar --server.port 8080 --categolj2.h2.datadir=./foobar

When you would like to use MySQL then:

    $ java -Xms512m -Xmx1g -jar categolj2-backend.jar --server.port 8080 --spring.jpa.database=MYSQL --spring.datasource.driverClassName=com.mysql.jdbc.Driver --spring.datasource.url=jdbc:mysql://localhost:3306/categolj2?zeroDateTimeBehavior=convertToNull --spring.datasource.username=root --spring.datasource.password=password

In this case, you have to run `create database categolj2` using `mysql` command in advance.

Other properties are explained in [Configurable properties](#configurable-properties).


## Experimental Docker Support

If Docker is not installed follow this: https://docs.docker.com/installation/

* Build Application
 ```sh
 cd categolj2-backend
 (Optional) mvn clean install -f frontend-ui/pom.xml -Dgpg.skip=true
 (Optional) mvn clean install -f backend-ui/pom.xml -Dgpg.skip=true
 cd backend-api
 mvn clean package
 cd target
 docker build -t categolj2-backend .
 ```

* Deploy to Docker Container
 ```sh
 docker images # List all Docker images
 docker run -p 8080:8080 -p 8443:8443 <image id>
 ```

## Backend APIs

Supported REST APIs are following.

Resource	| Method	| Path	| Description	| Pageable	| Authenticated
--------------- | ------------- | ----- | ------------- | ------------- | ----------------
Entries	| GET	| /api/v1/entries	| Get all entries	| × | 	
Entries	| GET	| /api/v1/entries?keyword={keyword}	| Search entries	| ×  | 		
Entries	| GET	| /api/v1/categories/{category}/entries	| Get all entries associated with the specified category.		 | ×  |
Entries	| GET	| /api/v1/users/{createdBy}/entries	| Get all entries created with the specified user.	| ×  |
Entries	| POST	| /api/v1/entries	| Create a new entry.	| | ×
Entry	| GET	| /api/v1/entries/{entryId}	| Get the specified entry.	 | |  	
Entry	| PUT	| /api/v1/entries/{entryId}	| Update the specified entry.	 | | ×	
Entry	| DELETE	 | /api/v1/entries/{entryId}	| Delete the specified entry.	| | × 
Entry Histories	| GET	| /api/v1/entries/{entryId}/histories	| Get all entry histories associated with the specified entry. | | × 	
Categories	| GET	| /api/v1/categories	| Get all categories.	| | 	
Categories	| GET	| /api/v1/categories?keyword={keyword}	| Search categories. | | × 		
Recent Posts	| GET	| /api/v1/recentposts	| Get entries updated recently.	 | |
Users	| GET	| /api/v1/users	| Get all users.	 | ×  |  × 	
Users	| POST	| /api/v1/users	| Create a new user.	 | |  × 	
User	| GET	| /api/v1/users/{username}	| Get the specified user. | | ×  		
User	| PUT	| /api/v1/users/{username}	| Update the specified user.  | | ×  		
User	| DELETE	| /api/v1/users/{username}	| Delete the specified user. | | ×  
User	| GET	| /api/v1/users/me	| Get login user.  | | ×
User	| PUT	| /api/v1/users/me	| Update login user. (not implemented yet)  | | ×  		
Links	| GET	| /api/v1/links	| Get all links.	 | | 	
Links	| POST	| /api/v1/links	| Create a new link. | | ×  		
Link	| GET	| /api/v1/links/{url}	| Get the specified link.	 | | 	
Link	| PUT	| /api/v1/links/{url}	| Update the specified link. | | ×  		
Link	| DELETE	| /api/v1/links/{url}	| Delete the specified link. | | ×  		
Files	| GET	| /api/v1/files	| Get all file summaries (not include file contents).  | ×  |  × 
Files	| POST	| /api/v1/files	Upload files. | | ×  
File	| GET	| /api/v1/files/{fileId}	| Get the specified file. | | 
File	| GET	| /api/v1/files/{fileId}?attachment	| Download the specified file (with prompt). | |
File	| DELETE	| /api/v1/files/{fileId}	| Delete the specified file. | |  × 
Books	| GET	| /api/v1/books?title={title}	| Search books by the given title using Amazon Product API		 | | ×  
Books	| GET	| /api/v1/books?keyword={keyword}	| Search books by the given keyword using Amazon Product API | | ×  		

## Admin Console Screenshots

Admin console is a single page application using AJAX/REST API mentioned above. 

### Dashboard

![Dashboard][1]

### Manage entries

#### Entry list
![Entry list][2]

#### Entry form
You can write contents using Markdown or raw HTML.

![Entry form][3]

#### Entry preview
Live preview is available.

![Entry preview][4]

#### File upload
You can upload files to use the entry on the fly.
HTML5 `multiple` attribute is supported.

![File upload][5]

#### Amazon search
You can insert book link using Amazon Product API.

![Amazon search][6]

run with `--aws.accesskey.id=<Your Accesskey ID for AWS> --aws.secret.accesskey=<Your Secret Accesskey for AWS> --aws.associate.tag=<Your Associate Tag>`

#### Entry search
Full text search is available.

![Entry search][7]

### Manage uploaded files

![Manage uploaded files][8]

### Manage users

#### User list

![User list][9]

#### User edit
Inline edit is available.

![User edit][10]

### Manage links

![Manage links][11]

## Setup frontend

The following sample fronted is embedded by default:

![Sample Frontend][12]

You can use your own frontend.

Create `static/index.html` at the same directory with `categol2-backend.jar` like bellow:

``` bash
$ find .
.
./categolj2-backend.jar
./static
./static/index.html
```

After run `java -jar categolj2-backend.jar`, you can find the following log which tells your own welcome page was picked.

``` bash
2014-12-20 20:48:13.126  INFO                                    --- [           main] o.s.w.s.c.a.WebMvcConfigurerAdapter      : Adding welcome page: file:/xxx/yyy/zzz/static/index.html
```

You can reuse embedded frontend. Embedded html/css/javascript are zipped in `categol2-backend.jar`.

``` bash
$ unzip -d foo categol2-backend.jar
$ unzip -d bar foo/lib/categolj2-embedded-frontend-ui*
$ cp -r bar/static .
```

## Configurable properties

Key | Description | Default value
--------------- | ------------- | ----- 
log.verbose | set this property `warn` if you want to suppress the output of verbose log. |
log.sql | set this property `warn` if you want to suppress SQL log. | debug
log.sql.result | set this property `debug` if I want to output SQL result log. | error
hibernate.search.default.indexBase | file path storing Lucene index | /tmp/lucene
aws.accesskey.id | access key ID for AWS | \<Your Accesskey ID for AWS\>
aws.secret.accesskey | secret accesskey for AWS | \<Your Secret Accesskey for AWS\>
aws.endpoint | AWS endpoint| https://ecs.amazonaws.jp
aws.associate.tag | Associate Tag for Amazon Affiliate | ikam-22
accesslog.disabled | Disable to write access logs | false

If you want suppress debug logs, run with like `--log.verbose=warn --log.sql=warn`.

See also Spring Boot's [common application properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html).

## License

Licensed under the Apache License, Version 2.0.

  [1]: ./screenshots/ss-dashboadrd.png
  [2]: ./screenshots/ss-entries.png
  [3]: ./screenshots/ss-entries-form.png
  [4]: ./screenshots/ss-entries-preview.png
  [5]: ./screenshots/ss-entries-upload.png
  [6]: ./screenshots/ss-entries-amazon.png
  [7]: ./screenshots/ss-entries-search.png
  [8]: ./screenshots/ss-uploads.png
  [9]: ./screenshots/ss-users.png
  [10]: ./screenshots/ss-users-inline-edit.png
  [11]: ./screenshots/ss-links.png
  [12]: ./screenshots/ss-frontend.png
  [13]: ./screenshots/ss-frontend-custom.png
  [14]: ./screenshots/ss-frontend-custom2.png
