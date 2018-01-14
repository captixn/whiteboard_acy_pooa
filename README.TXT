=========================
Whiteboard collaboratif JAVA 1 
=========================
REQUIRES JAVA 1.8

Principe de fonctionnement :
=========================
Nous avons opte pour une architecture Client-Serveur.
Quand le client dessine manuellement une forme (cercle vide et plein, rectangle vide et plein, trait a main levee, texte … ) en utilisant l’interface graphique qui lui est proposee, la forme est codee en chaine de caractere suivant le protocole que nous avons defini. Cette chaine de caracteres est envoyee au serveur, qui s’assure de sa conformite, puis la broadcast a tous les autres clients. Les clients decodent ensuite la chaine de caractere recue et dessinent la forme correspondante sur leur whiteboard. (le client qui a emis la commande ne dessine aussi que lorsqu’il recoit la commande du serveur, pour eviter tout dephasage). Des commandes speciales existent pour la connection “HL”, deconnexion “QT”, synchronisant “GH” ou encore nettoyage “RD”. 

Fonctionnalites :
=========================
* Dessin de cercles pleins et vides et de rectangles pleins et vides de taille souhaitee (a la souris), traits a main levee, texte, le tout avec la couleur souhaitee.
* Gomme.
* Mise a jour du whiteboard d’un client qui se serait connecte en retard
* Effacer tout le whiteboard avec la permission de TOUS les clients
* Pas de limitations en termes de nombres de clients
 
Comment executer ?
=========================
1)	Clone repository : git clone https://github.com/captixn/whiteboard_acy_pooa.git 
2)	cd  to the cloned repository 
3)	In a terminal, run: 
java -cp whiteboard.jar whiteboard_server.Main (this starts server on port 11237)
4)	In an other terminal, run:
java -cp whiteboard.jar whiteboard_client.Main (this starts a client on localhost:11237 and opens a window.)
5)	repeat above command to open more clients

By default clients connect to localhost on port 11237 
To try different settings, load src of packages whiteboard_client and whiteboard_server (src/) and manipulate in your IDE. Not non-core librairies needed. 
Browse code to see comments.
Loads Javadoc for a quick summary. 
 
Protocole :
=========================
DRabcdefghijklm : Dessiner un rectangle vide, a partir du point defini par les coordonnees (X = acb , Y = def), de longueur L = ghi et de largeur l=JKL, et de couleur m.
FRabcdefghijklm : Dessiner un rectangle plein, a partir du point defini par les coordonnees (X = acb , Y = def), de longueur L = ghi et de largeur l=JKL, et de couleur m.
DLabcdefghijklm : Dessiner une ligne entre le point defini par les coordonnees (X1 = acb , Y2= def) et le point defini par les coordonnees (X2 = acb , Y2= def) , et de couleur m.
DCabcdefghijklm : Dessiner un cercle vide a partir du point defini par les coordonnees (X = acb , Y = def), de rayon R = ghi ( le ‘jkl’ doit etre egal au ghi, sinon ce sera une ellipse ) , et de couleur m.
FCabcdefghijklm : Dessiner un cercle vide a partir du point defini par les coordonnees (X = acb , Y = def), de rayon R = ghi ( le ‘jkl’ doit etre egal au ghi, sinon ce sera une ellipse ) , et de couleur m.
DEabcdefghijklm : Gommer en bougeant la souris, en dessinant un cercle plein blanc centre sur le point de coordonnees (X = acb , Y = def), peu importe les ’ghi’ et ‘klm’ et ‘m’.
DTabcdef000000CHAINEm : Dessiner du texte, dans le point de coordonnees (X = acb , Y = def), le texte en l’occurrence est CHAINE, et de couleur m.
GH000000000000 : Get history, demande de l’historique de toutes les commandes qui ont ete broadcastes avant la connexion d’un client.
RC000000000000 : tout effacer
RD000000000000 : demande a tous les clients si on peut effacer tout le whiteboard ou pas
 
 
 

