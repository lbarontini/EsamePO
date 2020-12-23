# EsamePO

Il nostro progetto implementa una API di tipo REST che permette di elaborare informazioni fornite dal servizio [Domains-Index](https://api.domainsdb.info/v1).

## Funzionalità

In conformità allo stile REST, la nostra implementazione espone le seguenti funzionalità richiamabili tramite i metodi HTTP GET/POST:

### Lista di tutti i TLD censiti

Richiesta GET sul percorso `/listAll`, senza alcun parametro.

```bash
$ curl -s 'http://localhost:8080/listAll' | jq
[
  {
    "name": "furniture"
  },
  {
    "name": "dclk"
  },
  {
    "name": "tennis"
  },
  {
    "name": "azure"
  },
  {
    "name": "swiftcover"
  },
  [...]
  {
    "name": "spreadbetting"
  }
]
```

### Informazioni relative a un TLD scelto

Richiesta GET sul percorso `/info` col parametro `tld` fornito dall'utente

```bash
$ curl -s 'http://localhost:8080/info?tld=de' | jq
{
  "name": "de",
  "domainsCount": 0,
  "includes": [
    "de"
  ],
  "description": [
    "de : https://en.wikipedia.org/wiki/.de",
    "Confirmed by registry <ops@denic.de> (with technical",
    "reservations) 2008-07-01"
  ]
}
```

### TLD più numeroso, meno numeroso e media di domini presenti sui TLD nell'ultimo censimento

Richiesta GET sul percorso `/stats` col parametro opzionale `count` che indica numero di TLD da prendere in considerazione per la statistica.
Se il parametro non viene fornito, il valore predefinito è 10.

```bash
$ curl -s 'http://localhost:8080/stats' | jq
{
  "min": {
    "name": "android",
    "domainsCount": 3,
    "includes": null,
    "description": [
      "android : 2014-08-07 Charleston Road Registry Inc."
    ]
  },
  "max": {
    "name": "gifts",
    "domainsCount": 5163,
    "includes": null,
    "description": [
      "gifts : 2014-07-03 Binky Moon, LLC"
    ]
  },
  "average": 1231.6
}
```

### Quantità di domini in un TLD che contengono le parole fornite, ordinate per frequenza

Richiesta POST sul percorso `/stats` inviando in formato JSON (MIME type `application/json`) il TLD scelto e un array contenente le parole da selezionare.

```bash
$ curl -s -H "Content-Type: application/json" -d '{"tld":"de","words": ["apple", "chair", "fish"]}' 'http://localhost:8080/stats' | jq
[
  {
    "name": "de",
    "matchesCount": 381,
    "matchingWord": "apple"
  },
  {
    "name": "de",
    "matchesCount": 175,
    "matchingWord": "fish"
  },
  {
    "name": "de",
    "matchesCount": 34,
    "matchingWord": "chair"
  }
]
```

### Lista di TLD ordinati per numerosità, con la loro rispettiva descrizione

Richiesta GET sul percorso `/rank` col parametro opzionale `count` che indica numero di TLD da prendere in considerazione per la statistica.
Se il parametro non viene fornito, il valore predefinito è 10.

```bash
$ curl -s 'http://localhost:8080/rank?count=4' | jq
[
  {
    "name": "furniture",
    "domainsCount": 3091,
    "includes": null,
    "description": [
      "furniture : 2014-03-20 Binky Moon, LLC"
    ]
  },
  {
    "name": "tennis",
    "domainsCount": 2228,
    "includes": null,
    "description": [
      "tennis : 2014-12-04 Binky Moon, LLC"
    ]
  },
  {
    "name": "azure",
    "domainsCount": 32,
    "includes": null,
    "description": [
      "azure : 2014-12-18 Microsoft Corporation"
    ]
  },
  {
    "name": "dclk",
    "domainsCount": 3,
    "includes": null,
    "description": [
      "dclk : 2014-11-20 Charleston Road Registry Inc."
    ]
  }
]
```
