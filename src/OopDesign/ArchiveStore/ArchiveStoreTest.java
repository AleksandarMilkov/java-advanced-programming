package OopDesign.ArchiveStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %s doesn't exist", id));
    }
}

class ArchiveStore{
    private ArrayList<Archive> archives;
    private StringBuilder log;

    public ArchiveStore(){
        archives = new ArrayList<>();
        log = new StringBuilder();
    }

    void archiveItem(Archive item, LocalDate date){
        item.setDateArchived(date);
        archives.add(item);
        log.append(String.format("Item %s archived at %s%n", item.getId(), date));
    }

    void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive a = archives.stream()
                .filter(archive -> archive.getId()==id)
                .findFirst()
                .orElseThrow(() -> new NonExistingItemException(id));

        log.append(a.open(date));
    }

    public String getLog() {
        return log.toString();
    }
}

abstract class Archive{
    protected int id;
    protected LocalDate dateArchived;
    public Archive(int id){
        this.id = id;
    }

    public abstract String open(LocalDate date);

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
    public int getId(){
        return id;
    }
}

class LockedArchive extends Archive{
    private final LocalDate dateToOpen;
    public LockedArchive(int id, LocalDate dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String open(LocalDate date) {
        return (date.isBefore(dateToOpen))? String.format("Item %s cannot be opened before %s%n", id, dateToOpen) : String.format("Item %s opened at %s%n", id, date);

    }
}

class SpecialArchive extends Archive{
    private final int maxOpen;
    private int timesOpened;
    public SpecialArchive( int id, int maxOpen){
        super(id);
        this.maxOpen = maxOpen;
        timesOpened = 0;
    }

    @Override
    public String open(LocalDate date) {
        if(timesOpened<maxOpen) {
            timesOpened++;
            return String.format("Item %s opened at %s%n", id, date);
        }
        return String.format("Item %s cannot be opened more than %s times\n", id, maxOpen);
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}