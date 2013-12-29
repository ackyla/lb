class InitSchema < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      t.string :token
      t.string :name
      t.timestamps
    end

    create_table :locations do |t|
      t.float :latitude
      t.float :longitude
      t.references :user
      t.timestamps
    end

    create_table :territories do |t|
      t.float :latitude
      t.float :longitude
      t.float :radius
      t.references :owner
      t.timestamps
      t.datetime "expired_time"
    end

    create_table :detections do |t|
      t.references :location
      t.references :territory
      t.timestamps
    end

    create_table :invasions do |t|
      t.references :user
      t.references :territory
      t.timestamps
    end

    add_index :users, [:id, :token], :name => "index_users_on_id_and_token", :unique => true
  end

  def self.down
    drop_table :users
    drop_table :locations
    drop_table :detections
    drop_table :territories
  end
end
