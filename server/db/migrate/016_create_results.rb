class CreateResults < ActiveRecord::Migration
  def self.up
    create_table :results do |t|
      t.integer :score
      t.references :user
      t.references :room
      t.references :hit
      t.timestamps
    end
  end

  def self.down
    drop_table :results
  end
end
