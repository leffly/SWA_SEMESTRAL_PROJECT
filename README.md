# BOOKIFY
Bookify je služba, která agreguje různé služby poskytující vypůjčování nebo prodej knih ve formě brožové, elektronické (pdf, epub atd.) nebo mluvené slovo (.mp3)

## Scénáře použití
- Uživatel si vybere z aktuálně dostupných produktů produkty, které si chce koupit a následně si je vloží do košíku. (pokud vypadne jedna ze služeb, která poskytuje typ produktu, produkt bude nedostupný pro další akci).

Použité služby: BoughtService, BookService / AudioService

- Uživatel si zkontroluje dostupnost audioknihy XY. Zjistí, že audiokniha XY je nedostupná a zažádá si o sledování dostupnosti.

Použité služby: StoreAvailabilityService

- Uživatel si zapůjčil několik knih.

Použité služby: BookService,BorrowedService 

- Jedna ze služeb poskytující audioknihy přidala do své nabídky novou audioknihu.

Použité služby: AudioService

## Microservices
- BookService - služba pro poskytování brožových knih (aktualizace naskladnění, vkládání nových knih, odebírání knih)
- AudioService - služba pro poskytování audio knih

(EBookService - služba pro poskytování elektronických knih )

- StoreAvailabilityService - služba pro zjištení dostupnosti knih a audio knih.
- BorrowedService - služba pro zapůjčení knih.
- BoughtService - služba pro nákup zboži.

## Tým
| Jméno |
|------------------|
| David Löffler |
| Jiří Soběslavský |
| Artem Grigoryan |
| Bogdan Grigoryan |
