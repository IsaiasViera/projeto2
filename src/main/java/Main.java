import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Tde2");
        EntityManager em = emf.createEntityManager();

        // Criação das tabelas
        em.getTransaction().begin();
        em.createNativeQuery("CREATE TABLE IF NOT EXISTS aluno (id SERIAL PRIMARY KEY, matricula VARCHAR(255), nome VARCHAR(255));").executeUpdate();
        em.createNativeQuery("CREATE TABLE IF NOT EXISTS curso (id SERIAL PRIMARY KEY, codigo VARCHAR(255), nome VARCHAR(255), cargaHoraria INT);").executeUpdate();
        em.createNativeQuery("CREATE TABLE IF NOT EXISTS aluno_curso (aluno_id BIGINT, curso_id BIGINT);").executeUpdate();
        em.getTransaction().commit();

        // Criação dos cursos
        em.getTransaction().begin();
        Curso curso1 = new Curso();
        curso1.setCodigo("C1");
        curso1.setNome("Curso 1");
        curso1.setCargaHoraria(40);

        Curso curso2 = new Curso();
        curso2.setCodigo("C2");
        curso2.setNome("Curso 2");
        curso2.setCargaHoraria(30);

        em.persist(curso1);
        em.persist(curso2);
        em.getTransaction().commit();

        // Persistência dos alunos com relação muitos para muitos
        em.getTransaction().begin();
        Aluno aluno1 = new Aluno();
        aluno1.setMatricula("A1");
        aluno1.setNome("João");
        aluno1.setCursos(Arrays.asList(curso1));

        Aluno aluno2 = new Aluno();
        aluno2.setMatricula("A2");
        aluno2.setNome("Maria");
        aluno2.setCursos(Arrays.asList(curso2));

        em.persist(aluno1);
        em.persist(aluno2);
        em.getTransaction().commit();

        // Consulta do Curso do Aluno
        em.getTransaction().begin();
        Aluno alunoConsultado = em.find(Aluno.class, aluno1.getId());
        List<Curso> cursosDoAluno = alunoConsultado.getCursos();
        System.out.println("Cursos do aluno " + alunoConsultado.getNome() + ": " + cursosDoAluno.get(0).getNome());
        em.getTransaction().commit();

        // Consulta de Alunos do Curso
        em.getTransaction().begin();
        Curso cursoConsultado = em.find(Curso.class, curso1.getId());
        List<Aluno> alunosDoCurso = cursoConsultado.getAlunos();
        System.out.println("Alunos do curso " + cursoConsultado.getNome() + ":");
        if (alunosDoCurso != null) {
            for (Aluno aluno : alunosDoCurso) {
                System.out.println(aluno.getNome());
            }
        }
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
