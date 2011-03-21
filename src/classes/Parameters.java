package classes;

public class Parameters {

    // Attraction du plus proche
    public double e_w = 0.1;

    // Attraction des voisins
    public double e_n = 0.01;

    // Age max des arrêtes
    public int maxAge = 50;

    // Nombre max de nœuds
    public int maxNodes = 100;

    // Intervalle d'insertion des nœuds
    public int insertInterval = 200;

    // Coeficient d'erreur lors de la création d'un nœud
    public double alpha = 0.5;

    // Coefficient de diminution de l'erreur
    public double beta = 0.05;

    // Durée de vie d'un nœud (0 = infini)
    public int lifeexpectency = 0;

    // Perte de vie max d'un nœud dans l'erreur
    public int poison = 5;

    public Parameters() {
    }

    public static Parameters life(int lifeexpectency) {
        Parameters p = new Parameters();
        p.lifeexpectency = lifeexpectency;
        return p;
    }

    public static Parameters allButLife(double e_w, double e_n, int maxAge, int maxNodes,
                                 int insertInterval, double alpha, double beta) {
        Parameters p = new Parameters();
        p.e_w = e_w;
        p.e_n = e_n;
        p.maxAge = maxAge;
        p.maxNodes = maxNodes;
        p.insertInterval = insertInterval;
        p.alpha = alpha;
        p.beta = beta;
        return p;
    }

    public static Parameters attraction(double e_w, double e_n) {
        Parameters p = new Parameters();
        p.e_w = e_w;
        p.e_n = e_n;
        return p;
    }
}
