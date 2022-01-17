public class Noname {
    public String name;
    public String somethingElse;
    public int age;

    private Noname(String name, String somethingElse, int age) {
        this.name = name;
        this.somethingElse = somethingElse;
        this.age = age;
    }

    public static class NonameBuilder {
        private String name;
        private String somethingElse;
        private int age;

        public NonameBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public NonameBuilder setSomethingElse(String somethingElse) {
            this.somethingElse = somethingElse;
            return this;
        }

        public NonameBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public Noname build() {
            return new Noname(name, somethingElse, age);
        }
    }
}
