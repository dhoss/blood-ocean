export TOKEN=$(cat ~/blood-ocean-digitalocean-token.txt);
echo "DO TOKEN ${TOKEN}";

source ~/blood-ocean-spaces-credentials.sh;
echo "DO SPACES CREDENTIALS:";
echo "KEY ID: ${BLOOD_OCEAN_SPACES_KEY_ID}";
echo "SECRET :${BLOOD_OCEAN_SPACES_SECRET}";

cd ~/projects/bocean/src/main/resources/static/bocean/ && \
npm run build && \
cd ~/projects/bocean/ && \
mvn clean spring-boot:run
