# Application mobile de l'hôpital SoigneMoi

Cette application permet aux médecins de l'hôpital d'ajouter des avis et des prescriptions aux patients qui ont pris rendez-vous avec eux. Sur leur tableau de bord, chaque rendez-vous s'affiche sous forme d'une liste.
Les boutons "Avis" et "Prescriptions" sont présents sur chaque séjour de la liste. Les médecins peuvent également consulter depuis leur application mobile les avis et prescriptions auparavant ajoutés aux séjours.

## Installation sur votre smartphone Android

1. Pour afficher le fichier d'installation .apk, rendez-vous à l'adresse : https://github.com/AroldMauger/soignemoi-appli/releases/tag/0.1
2. Téléchargez le fichier d'installation soignemoi-mobile.apk sur votre téléphone. Un message de mise en garde contre un fichier potentiellement dangereux s'affiche. Choisissez de télécharger quand même.
3. Ouvrez le fichier .apk téléchargé et installez l'application. Un message de Google Play Protect vous propose d'analyser l'appli. Choisissez "plus de détails" puis "Installer sans analyser".
4. Lancez l'application : la page de connexion pour les médecins s'affichent.
Note 1 : pour vous connecter en tant que médecin, vous devriez d'abord ajouter un médecin depuis la session administrateur de l'application web. Un médecin se connecte depuis l'application mobile grâce à son nom de famille et son matricule. Pour plus d'informations sur l'ajout de médecins, consultez le fichier ReadMe du repository GitHub : https://github.com/AroldMauger/soignemoi-web.git
Note 2 : pour accéder au tableau de bord du médecin sur l'application mobile, il faut impérativement qu'un rendez-vous ait été réservé au préalable par un patient de l'hôpital. Pour plus d'informations sur la réservation des rendez-vous, consultez le fichier ReadMe du repository GitHub : https://github.com/AroldMauger/soignemoi-web.git
5. Une fois connecté au tableau de bord, vous pouvez en tant que médecin ajouter des avis et des prescriptions pour un rendez-vous donné en sélectionnant les crayons de couleur orange. Vous pouvez également consulter les avis et prescriptions qui ont déjà été ajoutés.
   
## Technologies utilisées

Le projet a été réalisé avec les technologies suivantes :

- Kotlin 
- Android Studio 
- Appel au backend Symfony de l'application web (https://soignemoi.alwaysdata.net/)

