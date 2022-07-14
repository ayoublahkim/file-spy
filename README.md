<div id="top"></div>



[![Stargazers][stars-shield]][stars-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">


<h3 align="center">File Spy</h3>

  <p align="center">

    Spring boot starter : File Watcher and File encryption through KEK and DEK 
    

  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project


The project consists on a solution for system directory watching that can be used in two ways

* Java FileWatch Service
* Cron jobs

Once the files read, they are encrypted through the following mecanism :

1) Creating a random symmetric key R on the air,
2) Encrypting the large file with the symmetric key R to create EF=Sym(F, R),
3) Encrypting the symmetric key R with an asymmetric RSA public key to create ER=ASym(PublicKey, R),
4) Sending the encrypted file EF alongside ER.

### Encryption:

```none
+---------------------+      +--------------------+
|                     |      |                    |
| generate random key |      |   the large file   |
|        (R)          |      |        (F)         |
|                     |      |                    |
+--------+--------+---+      +----------+---------+
|        |                     |
|        +------------------+  |
|                           |  |
v                           v  v
+--------+------------+     +--------+--+------------+
|                     |     |                        |
| encrypt (R) with    |     | encrypt (F)            |
| your RSA public key |     | with symmetric key (R) |
|                     |     |                        |
|  ASym(PublicKey, R) |     |     EF = Sym(F, R)     |
|                     |     |                        |
+----------+----------+     +------------+-----------+
|                             |
+------------+ +--------------+
| |
v v
+--------------+-+---------------+
|                                |
|   send this files to the peer  |
|                                |
|     ASym(PublicKey, R) + EF    |
|                                |
+--------------------------------+


```

### Decryption :
```none
+----------------+        +--------------------+
|                |        |                    |
| EF = Sym(F, R) |        | ASym(PublicKey, R) |
|                |        |                    |
+-----+----------+        +---------+----------+
|                             |
|                             |
|                             v
|   +-------------------------+-----------------+
|   |                                           |
|   |             restore key (R)               |
|   |                                           |
|   | R <= ASym(PrivateKey, ASym(PublicKey, R)) |
|   |                                           |
|   +---------------------+---------------------+
|                         |
v                         v
+---+-------------------------+---+
|                                 |
|       restore the file (F)      |
|                                 |
|      F <= Sym(Sym(F, R), R)     |
|                                 |
+---------------------------------+
```
Once files encrypted, 

Create an implementation of the interface **IFileListener**

The encrypted content can be processed in that implementation



### Built With

This section should list any major frameworks/libraries used to bootstrap your project. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.

* [![Springboot][Springboot]][Springboot]


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

MAVEN - JDK8+

### Installation


1. Clone the repo
   ```sh
   git clone https://github.com/ayoublahkim/file-spy.git
   ```
2. Install packages
   ```sh
   mvn install
   ```
3. You can now add the starter into your projects (pom.xml) !
   ```xml
     <dependency>
            <groupId>org.gov.lahkim</groupId>
            <artifactId>file-spy-starter</artifactId>
            <version>0.0.1</version>
     </dependency>
   ```

<p align="right">(<a href="#top">back to top</a>)</p>

4. Add configuration into your project's properties file:

_Example 1:_
```yml
file-starter:
  file-spy:
    public-key: (path to public.pem)
    private-key: (path to private.der)
    directory-write-path: some-path-1
    archive-directory-path: some-path-2 # optional
    watcher:
      directory-read-path: some-path-3
      regex-pattern: '*.pdf' # optional
```

_Example 2:_
```yml
file-starter:
  file-spy:
    public-key: (path to public.pem)
    private-key: (path to private.der)
    directory-write-path: some-path-1
    archive-directory-path: some-path-2 # optional
    cron:
      directory-read-path: some-path-3
      pattern: '* * * * * *'  #Every second
      regex-pattern: '*.pdf' # optional
```




<!-- USAGE EXAMPLES -->
## Usage


_For more examples, please refer to the [Documentation](https://example.com)_



<!-- CONTACT -->
## Contact

Ayoub LAHKIM - [@LINKEDIN](https://www.linkedin.com/in/ayoub-lahkim-934456122/)


<p align="right">(<a href="#top">back to top</a>)</p>







<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/ayoublahkim/file-spy/stargazers
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/ayoub-lahkim-934456122/
[product-screenshot]: images/screenshot.png
[Springboot]: https://img.shields.io/badge/springboot-000000?style=for-the-badge&logo=springboot&logoColor=green
[Springboot-url]: https://spring.io/projects/spring-boot
