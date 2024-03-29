# ISDOC.PDF

ISDOC.PDF je formát pro elektronickou reprezentaci vizuální i obsahové
stránky faktury. ISDOC.PDF je PDF soubor obsahující XML reprezentaci
faktury ve formátu ISDOC.

V této repozitory najdete podpůrné informace a příklady pro
implementaci podpory formátu ISDOC.PDF do vašich aplikací.

Formát ISDOC.PDF je definován jako součást
[ISDOC – Národního standardu pro elektronickou fakturaci](https://isdoc.cz/6.0.2/).

## Ukázky faktur ve formátu ISDOC.PDF

Ukázky dokumentů najdete
[v adresáři output](https://github.com/isdoc/isdoc.pdf/tree/main/examples/output). Jsou
vygenerovány z podkladů
[v adresáři input](https://github.com/isdoc/isdoc.pdf/tree/main/examples/input).

Pracujete-li na aplikaci, která generuje ISDOC.PDF dokumenty, rádi
vaše příklady přidáme k ostatním. Nové příklady nejlépe pošlete pomocí
[pull requestu](https://github.com/isdoc/isdoc.pdf/pulls).

## Generování ukázkových faktur ve formátu ISDOC.PDF

Pomocí následujícího příkazu můžete ze strukturované podoby faktury ve
formátu ISDOC a její vizuální podoby v PDF vygenerovat jeden ISDOC.PDF
soubor:

````
java -jar bin\isdoc-pdf.jar <vstup.pdf> <vstup.isdoc> <výstup.isdoc.pdf>
````

## Jak si prohlížet dokumenty ISDOC.PDF

Dokument ve formátu ISDOC.PDF je obyčejný dokument PDF a vizuální
podobu faktury si tak můžete prohlédnout v jakémkoliv prohlížeči
dokumentů PDF, včetně prohlížečů zabudovaných do webového
prohlížeče. Potřebujete-li se dostat ke vložené strukturované podobě
faktury v XML, je potřeba použít takový prohlížeč PDF, který umí
zobrazit přílohy, např. zdarma dostupný Adobe Acrobat Reader. Přílohy
jsou dostupné po kliknutí na záložku označenou "kancelářskou sponkou",
viz následující obrázek.

![Ukázka příloh zobrazených v Acrobat Readeru](acroread-attachments.png)

## Knihovny a nástroje umožňující generování ISDOC.PDF

* [isdoc-pdf](https://github.com/deltazero-cz/isdoc-pdf) - bash skript používající Ghostcript pro převod PDF a ISDOCu do ISDOC.PDF

* [node-isdoc-pdf](https://github.com/deltazero-cz/node-isdoc-pdf) - wrapper pro Node.js výše uvedeného skriptu, kromě tvorby ISDOC.PDF podporuje:
    * extrahování ISDOC z PDF
    * vytváření nových faktur ve formátu ISDOC
    * validování ISDOC vůči XSD schématu

ISDOC.PDF funguje na podobných principech jako německý formát ZUGFeRD
a francouzský Faktur-X. Následující nástroje tak poslouží i jako
inspirace při implementaci ISDOC.PDF.

* Python knihovna pro sloučení PDF and XML do PDF/A3
https://github.com/akretion/factur-x

* Java knihovna (ta posloužila i jako inspirace pro ukázkový příklad)
https://www.mustangproject.org/

* Generování pomocí komerční knihovny
https://www.pdflib.com/pdf-knowledge-base/zugferd-and-factur-x/

* Další OSS nástroje
https://zugferd.github.io/

